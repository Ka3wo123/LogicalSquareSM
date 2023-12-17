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
import pl.logicalsquare.IOproject.drawingLogic.fxmlControllers.StateMachineController;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

// todo poprawic wyswietlanie listview, dodac mape Map<String, List<String>> - stan - lista zmiennych
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
//    @FXML
//    private ListView<String> variablesListView;

    private Group spanTree;
    private boolean isGeneratable = false;
    private List<String> stateList;
    private Text lastClickedText;
    //    private Map<TextField, ListView<String>> variableMap;
    private TextField activeTextField;
    private List<Map<TextField, ListView<String>>> listOfMaps;
    private Map<TextField, ListView<String>> cornerAMap;
    private Map<TextField, ListView<String>> cornerEMap;
    private Map<TextField, ListView<String>> cornerIMap;
    private Map<TextField, ListView<String>> cornerOMap;
    private ListView<String> listViewA;
    private ListView<String> listViewE;
    private ListView<String> listViewI;
    private ListView<String> listViewO;

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


    public Main() {
        spanTree = new Group();
        stateList = new ArrayList<>();
//        variableMap = new HashMap<>();
        listOfMaps = new ArrayList<>();
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


        for (Node node : spanTree.getChildren()) {
            if (node instanceof Text text) {
                text.setOnMouseEntered(null);
                text.setOnMouseClicked(null);
            }
        }

        createSquare();


    }

    private void addTextFieldListener(TextField textField) {
        textField.textProperty().addListener((observable, oldValue, newValue) -> stateNameLabel.setText(newValue));
    }

    private void setTextFieldClickEvent(TextField textField) {
        textField.setOnMouseClicked(event -> {
            activeTextField = textField;
            stateNameLabel.setText(textField.getText());
            showVariablesListView(textField);
        });
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
                    if (!isPressed.get()) {
                        textNode.setFill(Color.TOMATO);
                    }
                    textNode.getScene().setCursor(Cursor.HAND);

                });

                textNode.setOnMouseExited(e -> {
                    if (!isPressed.get() || lastClickedText == null) {
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

    private Group createSquare() {
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


        listViewA = new ListView<>();
        listViewE = new ListView<>();
        listViewI = new ListView<>();
        listViewO = new ListView<>();

        for (int i = 0; i < 4; i++) {
            Map<TextField, ListView<String>> map = new HashMap<>();
            switch (i) {
                case 0 -> {
                    map.put(sentenceA, listViewA);
                    listOfMaps.add(map);
                }
                case 1 -> {
                    map.put(sentenceE, listViewE);
                    listOfMaps.add(map);
                }
                case 2 -> {
                    map.put(sentenceI, listViewI);
                    listOfMaps.add(map);
                }
                case 3 -> {
                    map.put(sentenceO, listViewO);
                    listOfMaps.add(map);
                }
            }
        }



//        cornerAMap.put(sentenceA, listViewA);
//        cornerEMap.put(sentenceE, listViewE);
//        cornerIMap.put(sentenceI, listViewI);
//        cornerOMap.put(sentenceO, listViewO);

//        listOfMaps.add(cornerAMap);
//        listOfMaps.add(cornerEMap);
//        listOfMaps.add(cornerIMap);
//        listOfMaps.add(cornerOMap);

        return object;
    }

    @FXML
    private void addItem() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle(stateNameLabel.getText() + " variables");
        dialog.setHeaderText("Enter variable name and select value:");

        ToggleGroup toggleGroup = new ToggleGroup();
        RadioButton trueRadioButton = new RadioButton("true");
        RadioButton falseRadioButton = new RadioButton("false");

        trueRadioButton.setToggleGroup(toggleGroup);
        falseRadioButton.setToggleGroup(toggleGroup);

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
                ListView<String> listView = getListViewForTextField(activeTextField);
                if (listView != null) {
                    listView.getItems().add(itemText);
                }
                return itemText;
            }
            return null;
        });

        dialog.showAndWait();
    }

    private ListView<String> getListViewForTextField(TextField textField) {
        for (Map<TextField, ListView<String>> map : listOfMaps) {
            if (map.containsKey(textField)) {
                return map.get(textField);
            }
        }
        return null;
    }

    private void showVariablesListView(TextField textField) {
        if (textField != null) {
            for (Map<TextField, ListView<String>> map : listOfMaps) {
                if (map.containsKey(textField)) {
                    ListView<String> listView = map.get(textField);
                    variablesVBox.getChildren().clear();
                    variablesVBox.getChildren().add(listView);
                    break;
                }
            }
        }
    }

//    @FXML
//    private void removeItem() {
//        int selectedIndex = variablesListView.getSelectionModel().getSelectedIndex();
//        if (selectedIndex >= 0) {
//            variablesListView.getItems().remove(selectedIndex);
//        }
//    }

    private TextField createLabelTextField() {
        TextField textField = new TextField();
        textField.setPrefWidth(100);
        textField.setPrefHeight(20);
        return textField;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        createSquare();
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