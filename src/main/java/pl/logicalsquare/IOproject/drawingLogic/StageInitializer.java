package pl.logicalsquare.IOproject.drawingLogic;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class StageInitializer implements ApplicationListener<SpanningTreeApplication.StageReadyEvent> {

    @Override
    public void onApplicationEvent(SpanningTreeApplication.StageReadyEvent event) {
        Stage stage = event.getStage();

        // Instantiate your SpanningTreeDrawer (replace YourSpanningTreeDrawer with your actual class)
        YourSpanningTreeDrawer spanningTreeDrawer = new YourSpanningTreeDrawer();

        // Create a Pane to hold your drawing
        Pane root = new Pane();

        // Add your spanning tree drawing to the root pane
        root.getChildren().add(spanningTreeDrawer.drawSpanningTree());

        // Create a scene with the root pane
        Scene scene = new Scene(root, 800, 600);

        // Set the scene and show the stage
        stage.setScene(scene);
        stage.show();
    }


}
