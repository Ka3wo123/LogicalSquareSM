package pl.logicalsquare.IOproject.drawingLogic.fxmlControllers;

import javafx.fxml.FXML;
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
    private Rectangle square;
    @FXML
    private VBox vbox;

    int hXstart = 100;
    int hYstart = 50;
    int hXend = 200;
    int hYend = 50;

    int vXstart = 100;
    int vYstart = 200;
    int vXend = 200;
    int vYend = 200;

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


        square = new Rectangle(70, 70);
        square.setX(50);
        square.setY(50);

        level++;

        A.add(AirplaneTrafficState.getState(level, "A"));
        E.add(AirplaneTrafficState.getState(level, "E"));
        I.add(AirplaneTrafficState.getState(level, "I"));
        O.add(AirplaneTrafficState.getState(level, "O"));

        //Line edge = createEdge(hXstart, hYstart, hXend, hYend);
        //spanTree.getChildren().add(edge);
        addAirplaneState(spanTree, A.get(level - 1), hXstart, hYstart);
        addAirplaneState(spanTree, E.get(level - 1), hXend, hYend);

        drawPane.setContent(vbox);

        //edge = createEdge(vXstart, vYstart, vXend, vYend);
        //spanTree.getChildren().add(edge);
        addAirplaneState(spanTree, I.get(level - 1), hXstart, hYstart);
        addAirplaneState(spanTree, O.get(level - 1), hXstart, hYstart);

//        drawPane.getChildren().add(spanTree);

        drawPane.setContent(spanTree);

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
        A = new ArrayList<>();
        E = new ArrayList<>();
        I = new ArrayList<>();
        O = new ArrayList<>();
        level = 0;
        hXstart = 100;
        hYstart = 50;
        hXend = 200;
        hYend = 50;
        vXstart = 100;
        vYstart = 200;
        vXend = 200;
        vYend = 200;
    }
}
