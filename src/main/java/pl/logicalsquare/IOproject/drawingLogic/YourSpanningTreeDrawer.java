package pl.logicalsquare.IOproject.drawingLogic;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import pl.logicalsquare.IOproject.domain.AirplaneTrafficState;

public class YourSpanningTreeDrawer {

    public Group drawSpanningTree() {
        // Create a Group to hold the drawing components
        Group group = new Group();

        // Example: Draw edges of a spanning tree using Lines
        Line edge1 = createEdge(100, 100, 200, 100);
        Line edge2 = createEdge(200, 100, 200, 200);
        Line edge3 = createEdge(200, 200, 100, 200);
        Line edge4 = createEdge(100, 200, 100, 100);

        // Add the edges to the group
        group.getChildren().addAll(edge1, edge2, edge3, edge4);

        addAirplaneState(group, AirplaneTrafficState.TAXIING, 100, 100);
        addAirplaneState(group, AirplaneTrafficState.IMMOBILISING, 200, 100);
        addAirplaneState(group, AirplaneTrafficState.STOP, 200, 200);
        addAirplaneState(group, AirplaneTrafficState.IMMOBILISING, 100, 200);


        return group;
    }

    private Line createEdge(double startX, double startY, double endX, double endY) {
        Line edge = new Line(startX, startY, endX, endY);
        edge.setStroke(Color.BLACK);
        edge.setStrokeWidth(2);
        return edge;
    }

    private void addAirplaneState(Group group, AirplaneTrafficState state, double x, double y) {
        // Create a Text element with the airplane state
        Text text = new Text(x-20, y - 5, state.toString());
        text.setFill(Color.GREEN);

        // Add the Text element to the group
        group.getChildren().add(text);
    }
}
