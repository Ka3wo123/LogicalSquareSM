package pl.logicalsquare.IOproject.drawingLogic;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class StageInitializer implements ApplicationListener<SpanningTreeApplication.StageReadyEvent> {

    @Override
    public void onApplicationEvent(SpanningTreeApplication.StageReadyEvent event) {
        Stage stage = event.getStage();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/init.fxml"));
            Scene scene = new Scene(loader.load());

            stage.setScene(scene);
            stage.setTitle("State machine generator");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
