package pl.logicalsquare.IOproject.drawingLogic.fxmlControllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

// na maszynie stanowej dodac na krawedziach przejscia/triggery, dodac funkcje wyswietlania eventu po najechaniu myszki na strzalke
// wygenerowac maszyne stanowa z tego drzewa rozpinajacego
// wybrac lisc drzewa rozpinajacego i w zaleznosci rozpinac drzewo o nowe stany
// w runtime dodawac stany recznie dla kwadratu i potem generowac drzewo z tych stanow i potem wybrac lisc i z tego liscia rekurencyjnie dodac stany generowac drzewo
public class Main implements Initializable {
    @FXML
    public Button drawTreeButton;
    @FXML
    private ScrollPane drawPane;
    @FXML
    private VBox spanTreePane;
    @FXML
    private Button nextButton;
    private Group spanTree;
    private boolean isGeneratable = false;
    @FXML
    private VBox vbox;

    private int posXstart;
    private int posXend;
    private int posYstart;
    private int posYend;

    private TextField sentenceA;
    private TextField sentenceE;
    private TextField sentenceI;
    private TextField sentenceO;

    private String textA;
    private String textE;
    private String textI;
    private String textO;

    private Text lastClickedText;
    private double lastMouseX, lastMouseY;


    public Main() {
        spanTree = new Group();
    }

    public void appendSquare() {
        isGeneratable = true;
        drawTreeButton.setDisable(false);

        createHollowSquare();
    }


    @FXML
    public void renderTree() {

//        addInitialState();

        drawTreeButton.setDisable(true);


        Line treeEdge1 = createEdge(posXstart, posYstart, posXend, posYend);
        Line treeEdge2 = createEdge(posXstart, posYstart, posXend + 300, posYend);
        Line treeEdge3 = createEdge(posXstart, posYstart, posXend + 600, posYend);


        spanTree.getChildren().addAll(treeEdge1, treeEdge2, treeEdge3);


        textA = sentenceA.getText();
        textI = sentenceI.getText();
        textE = sentenceE.getText();
        textO = sentenceO.getText();

        addStateToGroup(textA, textE, posXend, posYend + 20);
        addStateToGroup(textI, textO, posXend + 300, posYend + 20);
        addStateToGroup(textE, textO, posXend + 600, posYend + 20);

        sentenceA.setEditable(false);
        sentenceE.setEditable(false);
        sentenceI.setEditable(false);
        sentenceO.setEditable(false);

        lastClickedText = null;

        for (Node node : spanTree.getChildren()) {
            AtomicBoolean isPressed = new AtomicBoolean(false);
            if (node instanceof Text textNode) {
                textNode.setOnMouseClicked(event -> {
                    posXstart = (int) textNode.getX();
                    posXend = (posXstart - 300);
                    posYstart = (int) textNode.getY() + 25;

                    if (lastClickedText != null) {
                        lastClickedText.setFill(Color.BLACK);
                    }

                    textNode.setFill(Color.TOMATO);
                    isPressed.set(true);

                    lastClickedText = textNode;
                });
                textNode.setOnMouseEntered(e -> {
                    textNode.setFill(Color.TOMATO);

                });

                textNode.setOnMouseExited(e -> {
                    if (!isPressed.get() && lastClickedText == null) {
                        textNode.setFill(Color.BLACK);
                    }
                });
            }
        }

        posYend += 160;

        spanTreePane.getChildren().clear();
        spanTreePane.getChildren().addAll(spanTree);

    }


    private void addStateToGroup(String s1, String s2, int x, int y) {
        Text text = new Text(x, y, s1.concat(" &\n").concat(s2));
        text.setFill(Color.BLACK);

        spanTree.getChildren().add(text);
    }

    private Line createEdge(int startX, int startY, int endX, int endY) {
        Line edge = new Line(startX, startY, endX, endY);

        edge.setStroke(Color.BLACK);
        edge.setStrokeWidth(2);
        return edge;
    }

    private void addInitialState() {
        Text text = new Text(500, 5, "0");
        text.setFill(Color.BLACK);

        spanTreePane.getChildren().add(text);
    }


    public void clear() {
        drawTreeButton.setDisable(true);
        drawPane.setContent(null);
        vbox.getChildren().clear();

    }

    private Group createHollowSquare() {
        Group object = new Group();
        Rectangle square = new Rectangle(100, 100);

        square.setStroke(Color.BLACK);
        square.setFill(Color.WHITE);

        sentenceA = createLabelTextField();
        sentenceE = createLabelTextField();
        sentenceI = createLabelTextField();
        sentenceO = createLabelTextField();

        sentenceA.setLayoutX(square.getX() - 100);
        sentenceA.setLayoutY(square.getY() - 30);

        sentenceE.setLayoutX(square.getX() + square.getWidth());
        sentenceE.setLayoutY(square.getY() - 30);

        sentenceI.setLayoutX(square.getX() - 100);
        sentenceI.setLayoutY(square.getY() + square.getHeight() + 5);

        sentenceO.setLayoutX(square.getX() + square.getWidth());
        sentenceO.setLayoutY(square.getY() + square.getHeight() + 5);

        object.getChildren().addAll(square, sentenceA, sentenceE, sentenceI, sentenceO);
        vbox.getChildren().add(object);

        return object;
    }

    private TextField createLabelTextField() {
        TextField textField = new TextField();
        textField.setPrefWidth(100);
        textField.setPrefHeight(20);
        return textField;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        createHollowSquare();
//        addInitialState();
        spanTreePane.setOnScroll(this::handleScroll);
        posXstart = 500;
        posXend = 200;
        posYstart = 10;
        posYend = posYstart + 100;

    }

    private void handleScroll(ScrollEvent event) {
        if (event.isControlDown()) {
            double scaleFactor = (event.getDeltaY() > 0) ? 1.1 : 1 / 1.1;
            spanTreePane.setScaleX(spanTreePane.getScaleX() * scaleFactor);
            spanTreePane.setScaleY(spanTreePane.getScaleY() * scaleFactor);
            event.consume();
        }
    }


}


