package pl.logicalsquare.IOproject.drawingLogic.fxmlControllers;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import org.springframework.test.context.jdbc.Sql;
import pl.logicalsquare.IOproject.domain.AirplaneTrafficState;

import java.util.ArrayList;
import java.util.List;


public class PrimaryController {
    @FXML
    public Button generateButton;
    @FXML
    private ScrollPane drawPane;
    @FXML
    private Button nextButton;
    private Group spanTree;
    private boolean isGeneratable = false;
    private int level = 0;
    private List<AirplaneTrafficState> A;
    private List<AirplaneTrafficState> E;
    private List<AirplaneTrafficState> I;
    private List<AirplaneTrafficState> O;
    @FXML
    private VBox vbox;

    int hXstart = 200;
    int hYstart = 50;
    int hXend = 300;
    int hYend = 50;

    int vXstart = 200;
    int vYstart = 50;
    int vXend = 200;
    int vYend = 150;

    public PrimaryController() {
        spanTree = new Group();
        A = new ArrayList<>();
        E = new ArrayList<>();
        I = new ArrayList<>();
        O = new ArrayList<>();
        //addInitialState(spanTree);
    }

    @FXML
    public void appendState() {
        isGeneratable = true;
        generateButton.setDisable(false);


        level++;

        A.add(AirplaneTrafficState.getState(level, "A"));
        E.add(AirplaneTrafficState.getState(level, "E"));
        I.add(AirplaneTrafficState.getState(level, "I"));
        O.add(AirplaneTrafficState.getState(level, "O"));

        addAirplaneState(spanTree, A.get(level - 1), 195, 45);
        addAirplaneState(spanTree, E.get(level - 1), hXend + 2, hYend - 2);
        addAirplaneState(spanTree, I.get(level - 1), vXend - 5, vYstart + 5);
        addAirplaneState(spanTree, O.get(level - 1), vXend + 5, vYend + 5);


        // square creating
        Line edge = createEdge(hXstart, hYstart, hXend, hYend);
        Line edge2 = createEdge(vXstart, vYstart, vXend, vYend);
        Line edge3 = createEdge(hXstart, hYstart + 100, hXend, hYend + 100);
        Line edge4 = createEdge(vXstart + 100, vYstart, vXend + 100, vYend);
        Line edge5 = createEdge(hXstart, hYstart, hXend, hYend + 100);
        Line edge6 = createEdge(hXend, hYend, vXend, vYend);

        spanTree.getChildren().addAll(edge, edge2, edge3, edge4, edge5, edge6);

        vbox.setAlignment(Pos.TOP_CENTER);
        vbox.getChildren().add(spanTree);

        spanTree = new Group();


    }


    @FXML
    public void renderTree() {
        if (isGeneratable) {
            drawSpanningTree();
            //drawPane.getChildren().add(spanTree);
        }
    }

    private void drawSpanningTree() {


        generateButton.setDisable(true);

    }

    private Line createEdge(double startX, double startY, double endX, double endY) {
        Line edge = new Line(startX, startY, endX, endY);

        edge.setStroke(Color.BLACK);
        edge.setStrokeWidth(2);
        return edge;
    }


    private void addAirplaneState(Group group, AirplaneTrafficState state, double x, double y) {
        Text text = new Text(x - 15, y - 15, state.toString());
        text.setFill(Color.BLACK);

        group.getChildren().add(text);
    }

    private void addInitialState(Group group) {
        Text text = new Text(385, 85, "0");
        text.setFill(Color.BLACK);

        group.getChildren().add(text);
    }

    public void clear() {
        generateButton.setDisable(true);
        spanTree = new Group();
        addInitialState(spanTree);
        //drawPane.getChildren().clear();
        drawPane.setContent(null);
        vbox.getChildren().clear();
        A = new ArrayList<>();
        E = new ArrayList<>();
        I = new ArrayList<>();
        O = new ArrayList<>();
        level = 0;
    }
}
