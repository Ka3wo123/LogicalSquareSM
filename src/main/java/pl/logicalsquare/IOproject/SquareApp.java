package pl.logicalsquare.IOproject;

import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Duration;

import java.io.IOException;
/**
 * Główna klasa dla aplikacji JavaFX o nazwie SquareApp.
 */
public class SquareApp extends Application {
    /**
     * Punkt wejścia dla aplikacji JavaFX. Inicjalizuje i wyświetla główną stronę.
     *
     * @param stage Główny etap dla aplikacji.
     * @throws IOException Jeśli wystąpi błąd podczas ładowania głównej strony.
     */
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
    /**
     * Wyświetla główną stronę aplikacji.
     *
     * @param primaryStage Główny etap dla aplikacji.
     */
    private void showMainPage(Stage primaryStage) {
        try {
            FXMLLoader mainLoader = new FXMLLoader(getClass().getResource("/fxml/main_page.fxml"));
            String css = this.getClass().getResource("/css/style.css").toExternalForm();
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
    /**
     * Główny punkt wejścia dla aplikacji Java.
     *
     * @param args Argumenty wiersza poleceń.
     */
    public static void main(String[] args) {
        launch();
    }

}
