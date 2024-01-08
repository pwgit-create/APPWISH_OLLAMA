package pn.app_wish.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Callback;
import pn.app_wish.AppWish;
import pn.app_wish.constant.GUIConstants;
import pn.app_wish.util.AppWishUtil;
import pn.cg.datastorage.constant.PathConstants;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import static pn.app_wish.constant.GUIConstants.DEFAULT_STAGE_TITLE;


public class AppHistoryController implements Initializable {

    @FXML
    private ListView<File> fileListView;

    @FXML
    private Button selectButton;

    @FXML
    private Button btnStopApp;

    @FXML
    private Button btnMainScene;



    private Process executingJavaAppProcess;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        AnchorPane.setRightAnchor(btnMainScene,0d);
        AnchorPane.setRightAnchor(btnStopApp,80d);
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
        System.out.println("Executing java app on path -> " + AppWishUtil.removeClassPrefixFromString(selectedFile.getAbsolutePath()));

        System.out.println("File name:"+selectedFile.getName());
        System.out.println("Executing path:"+selectedFile.getAbsolutePath());


        try {
            ProcessBuilder processBuilder;
            if(AppWishUtil.isLinux())
           processBuilder = new ProcessBuilder("/bin/bash", "-c","java " +selectedFile.getAbsolutePath().replace(".class",".java"));
            else
                processBuilder = new ProcessBuilder("java.exe",selectedFile.getAbsolutePath().replace(".class",".java"));
            executingJavaAppProcess = processBuilder.inheritIO().start();

        }
        catch (IOException e) {
            System.out.println("RuntimeException while starting Java executable");
            throw new RuntimeException(e);
        }
    }



    private void listHistoryApplications() throws IOException {
        File selectedDirectory = new File(PathConstants.RESOURCE_PATH + "java_source_code_classes_tmp"+File.separator);
        List<File> files = AppWishUtil.filterOnClassPrefix(Arrays.stream(Objects.requireNonNull(selectedDirectory.listFiles()))
                .collect(Collectors.toList()));
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
    private void stopExecutedJavaApp(ActionEvent ae){

        this.executingJavaAppProcess.toHandle().destroy();
    }
}
