package pn.app_wish.controller;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;




public class SplashScreenController implements Initializable {
    @FXML
    private ImageView splashImage;


    @Override
    public void initialize(URL url, ResourceBundle rb) {

        splashImage.setImage(new Image("shooting_star.png"));
        PauseTransition delay = new PauseTransition(Duration.seconds(3));
        delay.setOnFinished(event -> {
            // Close the SplashScreen and show the main application stage
            Stage splashStage = (Stage) splashImage.getScene().getWindow();
            splashStage.close();

            try {
                Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("start.fxml")));
                Stage mainStage = new Stage();
                mainStage.setResizable(false);
                mainStage.setScene(new Scene(root));
                mainStage.show();
            } catch (IOException ex) {
                // Handle exception
            }
        });
        delay.play();
    }
}