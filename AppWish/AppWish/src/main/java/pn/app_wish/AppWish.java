package pn.app_wish;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.simple.SimpleLogger;

import pn.app_wish.constant.CodeEvent;
import pn.app_wish.constant.GUIConstants;
import pn.app_wish.constant.StaticAppWishConstants;
import pn.cg.app_system.AppSystem;
import pn.cg.app_system.code_generation.model.CompilationJob;
import pn.cg.datastorage.DataStorage;
import pn.cg.datastorage.ThreadPoolMaster;
import pn.cg.datastorage.constant.PathConstants;

import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Paths;

import java.util.LinkedList;
import java.util.List;


import static java.util.Objects.requireNonNull;
import static pn.app_wish.constant.GUIConstants.*;


public class AppWish extends Application {
    private static final Logger log = LoggerFactory.getLogger(AppWish.class);
    private static Stage mainStage;
    @FXML
    public TextField tf_input;
    @FXML
    public Label output_label;
    @FXML
    public Button btn_create_application;
    @FXML
    public Button btn_run_application;
    @FXML
    public Button btn_app_history;
    @FXML
    public Button btn_continue_on_application;
    @FXML
    public BorderPane bp_main;
    @FXML
    public Button btn_StopGeneratedApp;
    @FXML
    public ImageView logo;
    @FXML
    public Button btn_super_app_creation;

    private String javaExecutablePath;
    private Process executingJavaAppProcess;
    private boolean isCodeGenerationOnGoing = false;

    public static void main(String[] args) {
        launch(args);
    }

    public static Stage getMainStage() {
        return mainStage;
    }

    @Override
    public void init() throws Exception {
        super.init();


    }

    @Override
    public void stop() throws Exception {
        System.out.println("Exiting app");
        ThreadPoolMaster.getInstance().TerminateThreads();
        Platform.exit();
        super.stop();
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        System.setProperty(SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "DEBUG");
        Parent root = FXMLLoader.load(requireNonNull(getClass().getClassLoader().getResource(DEFAULT_FXML_FILE)));
        mainStage = primaryStage;
        mainStage.setResizable(false);
        primaryStage.setTitle(GUIConstants.DEFAULT_STAGE_TITLE);
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
        System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");

    }

    /**
     * AppWish GUI lifecycle
     */
    private void onAppWish(CodeEvent codeEvent) {
        isCodeGenerationOnGoing = true;
        // The File Object will be null if it's a new app request or have
        // a value if it is a continuous build from an existing app
        File file;

        List<String> contentOfExistingJavaFile;


        if (codeEvent == CodeEvent.CONTINUE_ON_EXISTING_APPLICATION) {
            file = showOpenFileDialog();
            // Cancellation of dialog (no file selected)
            if (file == null) {
                isCodeGenerationOnGoing = false; // Set the flag for code generation running in the AppWish GUI to false
                return; // Don´t proceed with method invocations to code-generator-ollama
            }
            contentOfExistingJavaFile = readTextByLinesFromFile(file);
        }
        //**codeEvent == CodeEvent.CREATE_APPLICATION**//
        else {
            contentOfExistingJavaFile = null;
            file = null;
        }


        ThreadPoolMaster.getInstance().getExecutor().execute(() -> {
            startGuiThread(codeEvent);

            // The two parameters after the codeEvent will be null on new app request
            // and have values on modify existing apps requests

            String pathToJavaFileIfModify = "";
            if (file != null) {
                pathToJavaFileIfModify = file.getAbsolutePath();
            }
            startCodeGeneration(codeEvent, pathToJavaFileIfModify, contentOfExistingJavaFile);

            waitForCompilationResult();

            handleCompilationResult();

        });

    }

    @FXML
    private void onRunJavaApp(ActionEvent ae) {
        btn_run_application.setVisible(false);
        btn_StopGeneratedApp.setVisible(true);

        if (javaExecutablePath != null) {
            System.out.println("Executing java app on path -> " + javaExecutablePath);
            try {
                ProcessBuilder pb;
                pb = new ProcessBuilder(StaticAppWishConstants.BASH_PATH, StaticAppWishConstants.C_ARGUMENT, StaticAppWishConstants.JAVA_TEXT + javaExecutablePath);
                executingJavaAppProcess = pb.inheritIO().start();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @FXML
    private void onViewAppHistory(ActionEvent ae) throws IOException {

        if(!isCodeGenerationOnGoing) {
            AnchorPane pane = FXMLLoader.load(
                    requireNonNull(getClass().getClassLoader().getResource(GUIConstants.APP_HISTORY_FXML_FILE)));
            Scene scene = new Scene(pane);
            mainStage.setScene(scene);
            mainStage.setTitle(APP_HISTORY_STAGE_TILE);
            mainStage.show();
        }
    }

    @FXML
    private void stopExecutedGeneratedJavaApp(ActionEvent ae) {

        this.executingJavaAppProcess.toHandle().destroy();
        btn_run_application.setVisible(true);
        btn_StopGeneratedApp.setVisible(false);
        setButtonGroupVisibilityForCodeGenerationButtons(true);

    }

    /**
     * Starts a thread that handles GUI Updates and
     * sends a message to the shared singleton that a new compilation job is about to start
     */
    private void startGuiThread(CodeEvent codeEvent) {
        DataStorage.getInstance().setCompilationJob(new CompilationJob(GUIConstants.DEFAULT_STAGE_TITLE));
        Platform.runLater(() -> {
            setButtonGroupVisibilityForCodeGenerationButtons(false);
            output_label.setVisible(true);

            switch (codeEvent) {
                case CREATE_APPLICATION:
                    codeEventCreateApplication();
                    break;
                case CONTINUE_ON_EXISTING_APPLICATION:
                    codeEventContinueAnApplication();
                    break;
            }

        });
    }

    /**
     * The method for the event CREATE_APPLICATION
     */
    private void codeEventCreateApplication(){
        if(!btn_StopGeneratedApp.isVisible()){
        setButtonGroupVisibilityForCodeGenerationButtons(false);
        output_label.setText(GUIConstants.GENERATING_CODE_DEFAULT_TEXT);
         }
    }
    /**
     * The method for the event CONTINUE_ON_EXISTING_APPLICATION
     */
    private void codeEventContinueAnApplication(){
        if(!btn_StopGeneratedApp.isVisible()) {
            setButtonGroupVisibilityForCodeGenerationButtons(false);
            output_label.setText(GUIConstants.CONTINUING_CODE_TEXT);
         }
        }

    /**
     * Create application button event
     */
    @FXML
    private void createApplication(ActionEvent ae) {

        if (!isCodeGenerationOnGoing) {
            setButtonGroupVisibilityToFalseForStartAndStopApplicationsButtons();
            onAppWish(CodeEvent.CREATE_APPLICATION);
        }
    }

    /**
     * Continue on existing application button event
     */
    @FXML
    private void continueOnExistingApplication(ActionEvent ae) {

        if (!isCodeGenerationOnGoing) {
            setButtonGroupVisibilityToFalseForStartAndStopApplicationsButtons();
            onAppWish(CodeEvent.CONTINUE_ON_EXISTING_APPLICATION);
        }
    }


    /**
     * Starts the AI Code-Generation if the text input field is not null
     */
    private void startCodeGeneration(CodeEvent codeEvent, String pathToJavaApp, List<String> contentOfExistingJavaFile) {

        if (tf_input != null) {
            // Stop displaying start/stop application buttons
            // Make a recursive call to AppSystem
            switch (codeEvent) {
                case CREATE_APPLICATION:
                    AppSystem.StartCodeGenerator(tf_input.getText());
                    break;
                case CONTINUE_ON_EXISTING_APPLICATION:
                    AppSystem.StartCodeGenerator(tf_input.getText(), requireNonNull(pathToJavaApp),
                            requireNonNull(contentOfExistingJavaFile));
                    break;
            }
        }
    }

    /**
     * Wait until the singleton in code-generator-ollama has a compilation job with a result
     */
    private void waitForCompilationResult() {

        while (!DataStorage.getInstance().getCompilationJob().isResult()) {
        }
    }

    private void waitUntilAllClassesOfTheSuperAppCreationHasBeenImplemented(){

        while(!DataStorage.getInstance().isSuperAppCreated()){}
    }

    /**
     * If a compilation result exist , check if the singleton in code-generator-ollama contains a path for an executable Java file
     * If the above is true , activate the "run application" button and remove the "generating code..." text
     */
    private void handleCompilationResult() {
        if (DataStorage.getInstance().getCompilationJob().isResult()) {
            javaExecutablePath = DataStorage.getInstance().getJavaExecutionPath();
            // Draw success or error texts, and show run app button
            Platform.runLater(() -> {
                if (DataStorage.getInstance().getJavaExecutionPath() != null) {
                    output_label.setVisible(false);
                    btn_run_application.setVisible(true);
                    setButtonGroupVisibilityForCodeGenerationButtons(true);
                    isCodeGenerationOnGoing = false;
                } else {
                    output_label.setText("Something went wrong :(");
                }
            });

        }
    }

    /**
     * Set the visibility for the create and continue application buttons
     */
    private void setButtonGroupVisibilityForCodeGenerationButtons(boolean isVisible) {
        btn_create_application.setVisible(isVisible);
        btn_continue_on_application.setVisible(isVisible);
        btn_super_app_creation.setVisible(isVisible);
    }

    /**
     * Set the visibility into false, for the start and stop application buttons (in the main gui)
     */
    private void setButtonGroupVisibilityToFalseForStartAndStopApplicationsButtons() {
        btn_run_application.setVisible(false);
        btn_StopGeneratedApp.setVisible(false);
    }


    private File showOpenFileDialog() {
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Choose A Java file to add functionality to");
            fileChooser.setInitialDirectory(new File(PathConstants.RESOURCE_PATH
                    + StaticAppWishConstants.FOLDER_NAME_OF_GENERATED_JAVA_APPLICATIONS + File.separator));
            // Create an extension filter that filters out .class files
            FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter(StaticAppWishConstants.CONTINUE_ON_APPLICATION_FILTER_ON_JAVA_EXTENSION_DESCRIPTION
                    , StaticAppWishConstants.CONTINUE_ON_APPLICATION_FILTER_ON_JAVA_EXTENSION);
            fileChooser.getExtensionFilters().add(extensionFilter);
            return fileChooser.showOpenDialog(getMainStage());
        } catch (Exception e) {
            log.error("Could not open the window for the file chooser");
            return null;
        }
    }

    private List<String> readTextByLinesFromFile(File file) {
        try {
            return Files.readAllLines(Paths.get(file.getAbsolutePath()));
        } catch (Exception e) {
            log.error("Could not read the lines of the specified file");
            return null;
        }
    }

    @FXML
    public void OnSuperAppCreationButton(ActionEvent ae) {

        isCodeGenerationOnGoing = true;
        DataStorage.getInstance().setCompilationJob(new CompilationJob(GUIConstants.DEFAULT_STAGE_TITLE));
        ThreadPoolMaster.getInstance().getExecutor().execute(() -> {

            Platform.runLater(() -> {
                setButtonGroupVisibilityForCodeGenerationButtons(false);
                setButtonGroupVisibilityToFalseForStartAndStopApplicationsButtons();
                output_label.setText(GENERATING_CODE_BASE_TEXT);
                output_label.setVisible(true);


                AppSystem.StartSuperAppGeneration(tf_input.getText(), true, false, new LinkedList<>(), false, null);


            });
        });
    }

}