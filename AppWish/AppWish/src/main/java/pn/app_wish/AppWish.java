package pn.app_wish;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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
import org.slf4j.simple.SimpleLogger;

import pn.app_wish.constant.CodeEvent;
import pn.app_wish.constant.GUIConstants;
import pn.cg.app_system.AppSystem;
import pn.cg.app_system.code_generation.model.CompilationJob;
import pn.cg.datastorage.DataStorage;
import pn.cg.datastorage.ThreadPoolMaster;

import java.io.IOException;

import java.util.Objects;

import static pn.app_wish.constant.GUIConstants.APP_HISTORY_STAGE_TILE;
import static pn.app_wish.constant.GUIConstants.DEFAULT_FXML_FILE;



public class AppWish extends Application {
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
    public Button btnStopGeneratedApp;
    
    private String javaExecutablePath;
    private Process executingJavaAppProcess;
    private boolean isCodeGenerationOnGoing =false;

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
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource(DEFAULT_FXML_FILE)));
        mainStage = primaryStage;
        primaryStage.setTitle(GUIConstants.DEFAULT_STAGE_TITLE);
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
        System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
    }


    /**
     * Appwish GUI lifecycle
     */
    private void onAppWish(CodeEvent codeEvent) {
        isCodeGenerationOnGoing = true;

            if(codeEvent == CodeEvent.CONTINUE_ON_EXISTING_APPLICATION){
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("test");
            File file =  fileChooser.showOpenDialog(getMainStage());
            //TODO: Continue from here
            }


        ThreadPoolMaster.getInstance().getExecutor().execute(() -> {
            startGuiThread(codeEvent);

            startCodeGeneration(codeEvent);

            waitForCompilationResult();

            handleCompilationResult();

        });

        //Wait for 1.5 sec 

        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {

            Thread.currentThread().interrupt();
        }
    }

    

    @FXML
    private void onRunJavaApp(ActionEvent ae) {
        btn_run_application.setVisible(false);
        btnStopGeneratedApp.setVisible(true);

        if (javaExecutablePath != null) {
            System.out.println("Executing java app on path -> " + javaExecutablePath);
            try {
                ProcessBuilder pb;

                pb = new ProcessBuilder("/bin/bash", "-c", "java " + javaExecutablePath);

                executingJavaAppProcess = pb.inheritIO().start();
            } catch (IOException e) {
                System.out.println("RuntimeException while starting Java executable");
                throw new RuntimeException(e);
            }
        }
    }

    @FXML
    private void onViewAppHistory(ActionEvent ae) throws IOException {
        AnchorPane pane = FXMLLoader.load(
                Objects.requireNonNull(getClass().getClassLoader().getResource(GUIConstants.APP_HISTORY_FXML_FILE)));
        Scene scene = new Scene(pane);
        mainStage.setScene(scene);
        mainStage.setTitle(APP_HISTORY_STAGE_TILE);
        mainStage.show();
    }

    @FXML
    private void stopExecutedGeneratedJavaApp(ActionEvent ae) {

        this.executingJavaAppProcess.toHandle().destroy();
        btn_run_application.setVisible(true);
        btnStopGeneratedApp.setVisible(false);
    }

    /**
     * Starts a thread that handles GUI Updates and
     * sends a message to the shared singleton that a new compilation job is about to start
     */
    private void startGuiThread(CodeEvent codeEvent) {
        DataStorage.getInstance().setCompilationJob(new CompilationJob(GUIConstants.DEFAULT_STAGE_TITLE));
        Platform.runLater(() -> {
            setButtonGroupVisibilityForCodeGenerationBtns(false);
            output_label.setVisible(true);
            
            switch(codeEvent){
                    case CREATE_APPLICATION:
                        output_label.setText("Generating code...");
                        break;
                    case CONTINUE_ON_EXISTING_APPLICATION:
                        output_label.setText("Generating code...\nContiune with existing application");
                        break;
            }

            setButtonGroupVisibilityForCodeGenerationBtns(false);
            btnStopGeneratedApp.setVisible(false);
        });
    }

    /**
     * Create application button event
     */
    @FXML
    private void createApplication(ActionEvent ae){

        if(!isCodeGenerationOnGoing)
        onAppWish(CodeEvent.CREATE_APPLICATION);

     }

    /**
     * Continue on existing application button event
     */
    @FXML
    private void continueOnExistingApplication(ActionEvent ae){
        
        if(!isCodeGenerationOnGoing)
        onAppWish(CodeEvent.CONTINUE_ON_EXISTING_APPLICATION);

     }


    /**
     * Starts the AI Code-Generation if the text input field is not null
     */
    private void startCodeGeneration(CodeEvent codeEvent) {

        if (tf_input != null) {
            // Make a recursive call to AppSystem
            switch(codeEvent){
                case CREATE_APPLICATION:
                    AppSystem.StartCodeGenerator(tf_input.getText());
                break;
                case CONTINUE_ON_EXISTING_APPLICATION:
                    AppSystem.StartCodeGenerator(tf_input.getText(),"javaClassName",null);
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

    /**
     * If a compilation result exist , check if the singleton in code-generator-ollama contains a path for a executable Java file
     * If the above is true , activate the "run application" button and remove the "generating code..." text
     */
    private void handleCompilationResult() {

        if (DataStorage.getInstance().getCompilationJob().isResult()) {
            javaExecutablePath = DataStorage.getInstance().getJavaExecutionPath();
            // Draw success or error texts, and show run app button
            Platform.runLater(() -> {
                if (DataStorage.getInstance().getJavaExecutionPath() != null) {
                    output_label.setText("Completed Successfully");
                    try {
                        Thread.sleep(2500);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                    output_label.setVisible(false);
                    btn_run_application.setVisible(true);
                    setButtonGroupVisibilityForCodeGenerationBtns(true);
                    isCodeGenerationOnGoing = false;
                } else {
                    output_label.setText("Error");
                    setButtonGroupVisibilityForCodeGenerationBtns(true);
                    isCodeGenerationOnGoing = false;
                }
            });
        }

    }
    /**
     * A button group broken out for reduce of redundancy
     */
    private void setButtonGroupVisibilityForCodeGenerationBtns(boolean isVisible){
            btn_create_application.setVisible(isVisible);
            btn_continue_on_application.setVisible(isVisible);
    }

}



