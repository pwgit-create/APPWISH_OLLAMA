package pn.app_wish.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pn.app_wish.AppWish;
import pn.app_wish.constant.GUIConstants;
import pn.app_wish.constant.StaticAppWishConstants;
import pn.app_wish.util.AppWishUtil;
import pn.cg.datastorage.constant.PathConstants;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

import static pn.app_wish.constant.GUIConstants.DEFAULT_STAGE_TITLE;
import static pn.app_wish.constant.StaticAppWishConstants.*;


public class AppHistoryController implements Initializable {

    private static final Logger log = LoggerFactory.getLogger(AppHistoryController.class);

    @FXML
    private ListView<File> fileListView;

    @FXML
    private Button selectButton;

    @FXML
    private Button btnStopApp;

    @FXML
    private Button btnMainScene;

    @FXML
    private Button btnDeleteApp;

    private Process executingJavaAppProcess;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        AnchorPane.setRightAnchor(btnMainScene, 0d);
        AnchorPane.setRightAnchor(btnStopApp, 80d);
        btnStopApp.setVisible(false);
        try {
            listHistoryApplications();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void handleSelectButtonAction(ActionEvent ae) {

        btnStopApp.setVisible(true);
        File selectedFile = fileListView.getSelectionModel().selectedItemProperty().getValue();

        try {
            ProcessBuilder processBuilder;

            final String r1 = ".class";
            final String r2 = ".java";
            final String executePath = selectedFile.getAbsolutePath().replace(r1, r2);

            processBuilder = new ProcessBuilder(BASH_PATH, C_ARGUMENT, JAVA_TEXT + executePath);
            executingJavaAppProcess = processBuilder.inheritIO().start();
        } catch (IOException e) {
            System.out.println("RuntimeException while starting Java executable");
            throw new RuntimeException(e);
        }
    }


    private void listHistoryApplications() throws IOException {
        File selectedDirectory = new File(PathConstants.RESOURCE_PATH + "java_source_code_classes_tmp" + File.separator);

        // Removes duplicate files from the generated java apps folder
        AppWishUtil.removeDuplicateFilesWithAnDollarSign(Arrays.stream(Objects.requireNonNull(selectedDirectory.listFiles()))
                .collect(Collectors.toList()));

        List<File> files = AppWishUtil.filterOnClassPrefix(Arrays.stream(Objects.requireNonNull(selectedDirectory.listFiles()))
                .collect(Collectors.toList()));

        List<File> continueAnAppFiles = AppWishUtil.retrieveFilesInContinueAnAppFolders();

        AppWishUtil.removeDuplicateFilesWithAnDollarSign(continueAnAppFiles);
        continueAnAppFiles = AppWishUtil.filterOnClassPrefix(continueAnAppFiles);
        files.addAll(continueAnAppFiles);

        fileListView.getItems().clear();
        fileListView.getItems().addAll(files);
        fileListView.setCellFactory(new Callback<>() {
            public ListCell<File> call(ListView<File> param) {
                return new ListCell<>() {
                    @Override
                    protected void updateItem(File item, boolean empty) {
                        super.updateItem(item, empty);
                        setText(item == null || empty ? null : item.getName());
                    }
                };
            }
        });
    }

    @FXML
    private void goToMainScene(ActionEvent ae) {
        if (this.executingJavaAppProcess != null) {
            this.executingJavaAppProcess.toHandle().destroy();
        }
        Pane pane;
        try {
            pane = FXMLLoader.load(
                    Objects.requireNonNull(getClass().getClassLoader().getResource(GUIConstants.DEFAULT_FXML_FILE)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Scene scene = new Scene(pane);
        Stage stage = AppWish.getMainStage();
        stage.setScene(scene);
        stage.setTitle(DEFAULT_STAGE_TITLE);
        stage.show();
    }

    @FXML
    private void stopExecutedJavaApp(ActionEvent ae) {
        btnStopApp.setVisible(false);
        this.executingJavaAppProcess.toHandle().destroy();
    }

    @FXML
    private void showConfirmDialogForDeletionOfAnJavaApplication(ActionEvent ae) {

        if (fileListView != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete an Java Application");
            alert.setHeaderText("Are you sure?\nYou can´t regret this action later!");
            alert.setContentText("Do you want to proceed with the action?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                deleteJavaApp(fileListView.getSelectionModel().selectedItemProperty().getValue());
            } else {
                log.info("Action Canceled");
            }
        }
    }

    private void deleteJavaApp(File classFileOfApplication) {

        Platform.runLater(() -> {

            try {

                // Delete the Class File of the chosen application
                Files.delete(classFileOfApplication.toPath());
                log.info("The class file of your selected application has been deleted");
            } catch (IOException e) {

                log.error("Could not delete the application");
            }
        });

        Platform.runLater(() -> {
            try {
                listHistoryApplications();
                fileListView.refresh();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }
}
