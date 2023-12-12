package pl.logicalsquare.IOproject.drawingLogic.fxmlControllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

// na maszynie stanowej dodac na krawedziach przejscia/triggery, dodac funkcje wyswietlania eventu po najechaniu myszki na strzalke

// TODO mozliwosc dodania zmiennych opisujacych stany, dodanie zdarzen jakos, generowanie maszyny stanowej, generowanie scenariusza
public class Main implements Initializable {
    @FXML
    public Button drawTreeButton;
    @FXML
    private ScrollPane drawPane;
    @FXML
    private Button generateMachineButton;
    @FXML
    private VBox spanTreePane;
    @FXML
    private Button nextButton;
    @FXML
    private VBox vbox;
    @FXML
    private VBox variablesVBox;
    @FXML
    private Label stateNameLabel;
    @FXML
    private ListView<String> variablesListView;

    private Group spanTree;
    private boolean isGeneratable = false;
    private List<String> stateList;
    private Text lastClickedText;
    private Map<TextField, ListView<String>> variableMap;

    private int posXstart;
    private int posXend;
    private int posYstart;
    private int posYend;

    //test

    private TextField sentenceA;
    private TextField sentenceE;
    private TextField sentenceI;
    private TextField sentenceO;

    private String textA;
    private String textE;
    private String textI;
    private String textO;



    public Main() {
        spanTree = new Group();
        stateList = new ArrayList<>();
        variableMap = new HashMap<>();
    }

    @FXML
    public void openWindowStateMachine(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/state_machine.fxml"));
        Parent root = loader.load();

        StateMachineController smc = loader.getController();
        smc.drawStateMachine(spanTree);

        Stage stage = new Stage();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("State machine");
        stage.show();
    }

    public void appendSquare() {
        isGeneratable = true;
        drawTreeButton.setDisable(false);

        createHollowSquare();


    }

    private void addTextFieldListener(TextField textField) {
        textField.textProperty().addListener((observable, oldValue, newValue) -> stateNameLabel.setText(newValue));
    }
    private void setTextFieldClickEvent(TextField textField) {
        textField.setOnMouseClicked(event -> stateNameLabel.setText(textField.getText()));
    }



    @FXML
    public void renderTree() {

        drawTreeButton.setDisable(true);
        generateMachineButton.setDisable(false);


        Line treeEdge1 = createEdge(posXstart, posYstart, posXend, posYend);
        Line treeEdge2 = createEdge(posXstart, posYstart, posXend + 300, posYend);
        Line treeEdge3 = createEdge(posXstart, posYstart, posXend + 600, posYend);


        spanTree.getChildren().addAll(treeEdge1, treeEdge2, treeEdge3);


        textA = sentenceA.getText();
        textI = sentenceI.getText();
        textE = sentenceE.getText();
        textO = sentenceO.getText();


        String s1 = addStateToGroup(textA, textI, posXend, posYend + 20);
        String s2 = addStateToGroup(textI, textO, posXend + 300, posYend + 20);
        String s3 = addStateToGroup(textE, textO, posXend + 600, posYend + 20);

        stateList.add(s1);
        stateList.add(s2);
        stateList.add(s3);

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
                    textNode.getScene().setCursor(Cursor.HAND);

                });

                textNode.setOnMouseExited(e -> {
                    if (!isPressed.get() && lastClickedText == null) {
                        textNode.setFill(Color.BLACK);
                    }
                    textNode.getScene().setCursor(Cursor.DEFAULT);
                });
            }

        }

        posYend += 160;

        spanTreePane.getChildren().clear();
        spanTreePane.getChildren().addAll(spanTree);

    }


    private String addStateToGroup(String s1, String s2, int x, int y) {
        Text text = new Text(x, y, s1.concat(" &\n").concat(s2));
        text.setFill(Color.BLACK);

        spanTree.getChildren().add(text);

        return text.getText();
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
        spanTreePane.getChildren().clear();
        vbox.getChildren().clear();
        spanTree.getChildren().clear();
        posXstart = 500;
        posXend = 200;
        posYstart = 10;
        posYend = posYstart + 100;

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

        addTextFieldListener(sentenceA);
        addTextFieldListener(sentenceE);
        addTextFieldListener(sentenceI);
        addTextFieldListener(sentenceO);

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

        setTextFieldClickEvent(sentenceA);
        setTextFieldClickEvent(sentenceE);
        setTextFieldClickEvent(sentenceI);
        setTextFieldClickEvent(sentenceO);

        ListView<String> listViewA = new ListView<>();
        ListView<String> listViewE = new ListView<>();
        ListView<String> listViewI = new ListView<>();
        ListView<String> listViewO = new ListView<>();

        variableMap.put(sentenceA, listViewA);
        variableMap.put(sentenceE, listViewE);
        variableMap.put(sentenceI, listViewI);
        variableMap.put(sentenceO, listViewO);

        return object;
    }

    @FXML
    private void addItem() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle(stateNameLabel.getText().concat(" variables"));
        dialog.setHeaderText("Enter variable name and select value:");

        ToggleGroup toggleGroup = new ToggleGroup();
        RadioButton trueRadioButton = new RadioButton("true");
        RadioButton falseRadioButton = new RadioButton("false");

        trueRadioButton.setToggleGroup(toggleGroup);
        falseRadioButton.setToggleGroup(toggleGroup);

        trueRadioButton.setSelected(true);

        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(5);

        gridPane.add(new Label("Variable:"), 0, 0);
        gridPane.add(dialog.getEditor(), 1, 0);
        gridPane.add(trueRadioButton, 1, 1);
        gridPane.add(falseRadioButton, 2, 1);

        dialog.getDialogPane().setContent(gridPane);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton.getButtonData() == ButtonType.OK.getButtonData()) {
                RadioButton selectedRadioButton = (RadioButton) toggleGroup.getSelectedToggle();
                String itemText = dialog.getEditor().getText() + " - " + selectedRadioButton.getText();
                variablesListView.getItems().add(itemText);
                return itemText;
            }
            return null;
        });

        dialog.showAndWait();


    }

    @FXML
    private void removeItem() {
        int selectedIndex = variablesListView.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            variablesListView.getItems().remove(selectedIndex);
        }
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


