package pl.logicalsquare.IOproject;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class SquareApp extends Application {
    @Override
    public void start(Stage stage) {
        showMainPage(stage);
    }

    private void showMainPage(Stage primaryStage) {
        try {
            FXMLLoader mainLoader = new FXMLLoader(getClass().getResource("/fxml/main_page.fxml"));
            String css = Objects.requireNonNull(this.getClass().getResource("/css/style.css")).toExternalForm();
            Scene mainScene = new Scene(mainLoader.load(), javafx.scene.paint.Color.TRANSPARENT);
            mainScene.getStylesheets().add(css);
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
