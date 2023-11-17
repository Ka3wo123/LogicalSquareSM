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

// na maszynie stanowej dodac na krawedziach przejscia/triggery, dodac funkcje wyswietlania eventu po najechaniu myszki na strzalke
// wygenerowac maszyne stanowa z tego drzewa rozpinajacego
// wybrac lisc drzewa rozpinajacego i w zaleznosci rozpinac drzewo o nowe stany
// w runtime dodawac stany recznie dla kwadratu i potem generowac drzewo z tych stanow i potem wybrac lisc i z tego liscia rekurencyjnie dodac stany generowac drzewo
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

    private int hXstart = 200;
    private int hYstart = 50;
    private int hXend = 300;
    private int hYend = 50;

    private int vXstart = 200;
    private int vYstart = 50;
    private int vXend = 200;
    private int vYend = 150;

    private int posXstart = 385;
    private int posXend = 50;
    private int posYstart = 85;
    private int posYend = 200;


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

        level++;

        A.add(AirplaneTrafficState.getState(level, "A"));
        E.add(AirplaneTrafficState.getState(level, "E"));
        I.add(AirplaneTrafficState.getState(level, "I"));
        O.add(AirplaneTrafficState.getState(level, "O"));

        A.get(level - 1).setStatus(true);
        // for E -> !a or !e
        E.get(level - 1).setStatus(!A.get(level - 1).getIsTrue() || !E.get(level - 1).getIsTrue());
        // for O a xor o
        O.get(level - 1).setStatus(!A.get(level - 1).getIsTrue());
        // for I a and !i
        I.get(level - 1).setStatus(A.get(level - 1).getIsTrue() && !I.get(level - 1).getIsTrue());

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


        square.getChildren().addAll(edge, edge2, edge3, edge4, edge5, edge6);

        vbox.getChildren().add(square);

        Line treeEdge1 = createEdge(posXstart, posYstart, posXend, posYend);
        Line treeEdge2 = createEdge(posXstart, posYstart, posXend + 320, posYend);
        Line treeEdge3 = createEdge(posXstart, posYstart, posXend + 600, posYend);

        spanTree.getChildren().addAll(treeEdge1, treeEdge2, treeEdge3);

        // A&I
        addStatesForSituation(spanTree, A.get(level - 1), I.get(level - 1), posXend - 15, posYend + 15);

        // E&O
        addStatesForSituation(spanTree, E.get(level - 1), O.get(level - 1),posXend + 285, posYend + 15);

        // I&O
        addStatesForSituation(spanTree, I.get(level - 1), O.get(level - 1), posXend + 585, posYend + 15);

        posYstart += 180;
        posYend += 180;

        square = new Group();
    }


    @FXML
    public void renderTree() {
        if (isGeneratable) {
            addInitialState();
            spanTreePane.getChildren().add(spanTree);
            generateButton.setDisable(true);
        }
    }

    private Line createEdge(double startX, double startY, double endX, double endY) {
        Line edge = new Line(startX, startY, endX, endY);

        edge.setStroke(Color.BLACK);
        edge.setStrokeWidth(2);
        return edge;
    }

    private void addStatesForSituation(Group group, AirplaneTrafficState state1, AirplaneTrafficState state2, double x, double y) {
        Text text = new Text(x, y, state1.toString().concat(" &\n").concat(state2.toString()));
        text.setFill(Color.BLACK);

        group.getChildren().add(text);
    }

    private void addAirplaneState(Group group, AirplaneTrafficState state, double x, double y) {
        Text text = new Text(x, y, state.toString().concat("(").concat(String.valueOf(state.getIsTrue())).concat(")"));
        text.setFill(Color.BLACK);

        group.getChildren().add(text);
    }

    private void addInitialState() {
        Text text = new Text(385, 82, "0");
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
