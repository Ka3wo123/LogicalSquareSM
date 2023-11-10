package pl.logicalsquare.IOproject.drawingLogic.fxmlControllers;

import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import pl.logicalsquare.IOproject.domain.AirplaneTrafficState;

import java.util.ArrayList;
import java.util.List;


public class PrimaryController {
    @FXML
    public Button generateButton;
    @FXML
    private ScrollPane drawPane;
    @FXML
    private Pane spanTreePane;
    @FXML
    private Button nextButton;
    private Group square;
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
        square = new Group();
        spanTree = new Group();
        A = new ArrayList<>();
        E = new ArrayList<>();
        I = new ArrayList<>();
        O = new ArrayList<>();
    }

    @FXML
    public void appendState() {
        isGeneratable = true;
        generateButton.setDisable(false);
        addInitialState();


        level++;

        A.add(AirplaneTrafficState.getState(level, "A"));
        E.add(AirplaneTrafficState.getState(level, "E"));
        I.add(AirplaneTrafficState.getState(level, "I"));
        O.add(AirplaneTrafficState.getState(level, "O"));

        A.get(level - 1).setStatus(false);
        // for E -> !a or !e
        E.get(level - 1).setStatus(!A.get(level-1).getIsTrue() || !E.get(level - 1).getIsTrue());
        // for O a xor o
        O.get(level - 1).setStatus(!A.get(level-1).getIsTrue());
        // for I a and !i
        I.get(level - 1).setStatus(A.get(level-1).getIsTrue() && I.get(level - 1).getIsTrue());

        /* when A true should be
        true
        false
        true
        false
        when A false then
        false
        true
        false
        true
         */

        System.out.println("A " + A.get(level - 1).getIsTrue());
        System.out.println("E " + E.get(level - 1).getIsTrue());
        System.out.println("I " +I.get(level - 1).getIsTrue());
        System.out.println("O " +O.get(level - 1).getIsTrue());

        addAirplaneState(square, A.get(level - 1), hXstart - 55, hYstart - 10);
        addAirplaneState(square, E.get(level - 1), hXend + 5, hYend - 10);
        addAirplaneState(square, I.get(level - 1), hXstart - 55, vYend + 15);
        addAirplaneState(square, O.get(level - 1), hXend, vYend + 15);

        // square creating
        Line edge = createEdge(hXstart, hYstart, hXend, hYend);
        Line edge2 = createEdge(vXstart, vYstart, vXend, vYend);
        Line edge3 = createEdge(hXstart, hYstart + 100, hXend, hYend + 100);
        Line edge4 = createEdge(vXstart + 100, vYstart, vXend + 100, vYend);
        Line edge5 = createEdge(hXstart, hYstart, hXend, hYend + 100);
        Line edge6 = createEdge(hXend, hYend, vXend, vYend);

//        if(A.get(level-1).getIsTrue() && I.get(level-1).getIsTrue()) {
//            vbox.getChildren().add(new Text("A&I"));
//        } else if(E.get(level-1).getIsTrue() && O.get(level-1).getIsTrue()) {
//            vbox.getChildren().add(new Text("E&O"));
//        } else if(I.get(level-1).getIsTrue() && O.get(level-1).getIsTrue()){
//            vbox.getChildren().add(new Text("I&O"));
//        }

        square.getChildren().addAll(edge, edge2, edge3, edge4, edge5, edge6);

        vbox.getChildren().add(square);

        square = new Group();


    }


    @FXML
    public void renderTree() {
        if (isGeneratable) {
            spanTreePane.getChildren().add(spanTree);
            drawSpanningTree();
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
        Text text = new Text(x, y, state.toString().concat("(").concat(String.valueOf(state.getIsTrue())).concat(")"));
        text.setFill(Color.BLACK);

        group.getChildren().add(text);
    }

//    private void addStatus(Group group, AirplaneTrafficState state, double x, double y) {
//        Text text = new Text(x - 15, y - 15, s);
//
//
//        group.getChildren().add(text);
//    }

    private void addInitialState() {
        Text text = new Text(385, 85, "0");
        text.setFill(Color.BLACK);

        spanTreePane.getChildren().add(text);
    }

    public void clear() {
        generateButton.setDisable(true);
        square = new Group();
//        addInitialState(spanTree);
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
