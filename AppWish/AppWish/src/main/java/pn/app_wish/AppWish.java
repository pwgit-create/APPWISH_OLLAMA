package pn.app_wish;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.slf4j.simple.SimpleLogger;
import pn.app_wish.constant.GUIConstants;
import pn.app_wish.util.AppWishUtil;
import pn.cg.app_system.AppSystem;
import pn.cg.app_system.code_generation.model.CompilationJob;
import pn.cg.datastorage.DataStorage;
import pn.cg.datastorage.ThreadPoolMaster;

import java.io.IOException;

import java.util.Objects;


import static pn.app_wish.constant.GUIConstants.APP_HISTORY_STAGE_TILE;
import static pn.app_wish.constant.GUIConstants.DEFAULT_FXML_FILE;


public class AppWish extends Application  {
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
    public BorderPane bp_main;
    @FXML
    public Button btnStopGeneratedApp;
    private String javaExecutablePath;

    private Process executingJavaAppProcess;


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

    @FXML
    private void onAppWish(ActionEvent ae) {

        ThreadPoolMaster.getInstance().getExecutor().execute(() -> {

            DataStorage.getInstance().setCompilationJob(new CompilationJob(GUIConstants.DEFAULT_STAGE_TITLE));
            Platform.runLater(() -> {
                btn_create_application.setVisible(false);
                output_label.setVisible(true);
                output_label.setText("Generating code...");
                btn_run_application.setVisible(false);
                btnStopGeneratedApp.setVisible(false);


            });

            if (tf_input != null) {
                // Make a recursive call to AppSystem
                AppSystem.StartCodeGenerator(tf_input.getText(), true, false);

                while (!DataStorage.getInstance().getCompilationJob().isResult()) {
                }
                if (DataStorage.getInstance().getCompilationJob().isResult()) {
                    javaExecutablePath = DataStorage.getInstance().getJavaExecutionPath();
                    // Draw success or error texts, and show run app button
                    Platform.runLater(() -> {
                        if (DataStorage.getInstance().getJavaExecutionPath() != null) {
                            output_label.setText("Completed Successfully");
                            try {
                                Thread.sleep(2500);
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                            output_label.setVisible(false);
                            btn_run_application.setVisible(true);
                            btn_create_application.setVisible(true);
                        } else {
                            output_label.setText("Error");
                            btn_create_application.setVisible(true);
                        }
                    });
                }
            }
        });
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {

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
                if(AppWishUtil.isLinux())
                 pb = new ProcessBuilder("/bin/bash", "-c", "java " + javaExecutablePath);
                else
                    pb = new ProcessBuilder("java.exe",javaExecutablePath);
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


}


