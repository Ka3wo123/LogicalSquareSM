package pl.logicalsquare.IOproject;

import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

public class SquareApp extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        showMainPage(stage);
//        FXMLLoader splashLoader = new FXMLLoader(SquareApp.class.getResource("/fxml/splash_screen.fxml"));
//        Scene splashScene = new Scene(splashLoader.load(), Color.TRANSPARENT);
//        Stage splashStage = new Stage();
//        splashStage.setScene(splashScene);
//
//        PauseTransition pauseTransition = new PauseTransition(Duration.seconds(2));
//        pauseTransition.setOnFinished(e -> {
//            FadeTransition fadeTransition = new FadeTransition(Duration.seconds(3), splashScene.getRoot());
//            fadeTransition.setFromValue(1);
//            fadeTransition.setToValue(0);
//
//            fadeTransition.setOnFinished(event -> {
//                splashStage.close();
//                showMainPage(stage);
//            });
//            fadeTransition.play();
//        });
//
//        pauseTransition.play();
//        splashStage.setTitle("State machine generator");
//        splashStage.show();
    }

    private void showMainPage(Stage primaryStage) {
        try {
            FXMLLoader mainLoader = new FXMLLoader(getClass().getResource("/fxml/main_page.fxml"));
            Scene mainScene = new Scene(mainLoader.load(), javafx.scene.paint.Color.TRANSPARENT);
            primaryStage.setScene(mainScene);
            primaryStage.setTitle("State machine generator");

            primaryStage.setMaximized(true);

            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch();
    }

}
