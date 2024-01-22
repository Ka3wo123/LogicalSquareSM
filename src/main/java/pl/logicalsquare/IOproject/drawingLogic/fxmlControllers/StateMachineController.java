package pl.logicalsquare.IOproject.drawingLogic.fxmlControllers;

import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGeometry;
import com.mxgraph.view.mxGraph;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.*;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.util.Duration;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Secondary controller for StateMachine window. It contains interactive state machine diagram on which transitions are
 * user-defined, trigger button which allows to trace flow of the states and scenario also. Furthermore, user can trace
 * states' variables' values depending on current state.
 */
public class StateMachineController {

    @FXML
    private Pane stateMachinePane;
    @FXML
    private ScrollPane scrollPane;
    private Group stateMachine;
    private double x = 100;
    private double y = 100;
    @FXML
    private Label scenarioLabel;
    private static int levelCount = 0;
    private StackPane sourceState;
    private List<StackPane> stateTrack;
    private Map<StackPane, List<String>> variablesMap;
    private int currentTransitionIndex = 0;

    public StateMachineController() {
        stateMachine = new Group();
        sourceState = new StackPane();
        stateTrack = new ArrayList<>();
        variablesMap = new HashMap<>();

    }

    public void handleExit(ActionEvent event) {
        Stage stage = (Stage) stateMachinePane.getScene().getWindow();
        stage.close();
    }

    public void handleSave(ActionEvent event) {
        saveScreenshot();
    }

    private void saveScreenshot() {
        WritableImage snapshot = stateMachinePane.snapshot(new SnapshotParameters(), null);

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Screenshot");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PNG files (*.png)", "*.png"));

        File file = fileChooser.showSaveDialog(stateMachinePane.getScene().getWindow());

        if (file != null) {
            try {
                ImageIO.write(SwingFXUtils.fromFXImage(snapshot, null), "png", file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Draws interactive state machine diagram with expanding transitions (those between situations).
     * @param graph mxGraph of states built in Main window
     * @param listOfMap Variables describing states as list.
     * @param statesList Merged states from ternary tree.
     * @param expansionStates States from which will it expand.
     */
    public void drawStateMachine(mxGraph graph, List<Map<TextField, ListView<String>>> listOfMap, List<String> statesList, List<String> expansionStates) {
        Object parent = graph.getDefaultParent();

        StringBuilder scenario = new StringBuilder();

        int statesCount = 0;
        int totalStates = statesList.size();

        sourceState = null;

        if (totalStates > 0) {
            for (int index = 0; index < totalStates; index++) {
                String text = statesList.get(index);
                String[] texts = text.split("\n");

                if (statesCount == 2 || index == totalStates - 1) {
                    statesCount = -1;
                    scenario.append("\"").append(texts[0]).append(" ").append(texts[1]).append("\"").append("; ").append(index == totalStates - 1 ? "" : " and then");
                } else {
                    scenario.append("\"").append(texts[0]).append(" ").append(texts[1]).append("\"").append(" and then ");
                }

                statesCount++;
            }

            scenarioLabel.setText(scenario.toString());
        }


        StackPane initialStackPane = new StackPane();
        Circle initialState = new Circle(10);
        initialState.setFill(Color.BLACK);
        initialStackPane.getChildren().add(initialState);
        initialStackPane.setLayoutX(x + 300);
        initialStackPane.setLayoutY(y - 80);

        stateMachinePane.getChildren().add(initialStackPane);

        Object[] cells = graph.getChildCells(parent);

        int k = 0;
        for (int i = 0; i < cells.length; i++) {
            Object cell = cells[i];

            if (i % 3 == 0 && i != 0) {
                k++;
            }

            if (cell instanceof mxCell mxCell) {
                ListView<String> list1 = null;
                ListView<String> list2 = null;
                String textToSet = mxCell.getValue().toString();

                if (i % 3 == 0) {
                    list1 = listOfMap.get(i + k).entrySet().iterator().next().getValue();
                    list2 = listOfMap.get(i + 2 + k).entrySet().iterator().next().getValue();
                } else if (i % 3 == 1) {
                    list1 = listOfMap.get(i + 1 + k).entrySet().iterator().next().getValue();
                    list2 = listOfMap.get(i + 2 + k).entrySet().iterator().next().getValue();
                } else if (i % 3 == 2) {
                    list1 = listOfMap.get(i - 1 + k).entrySet().iterator().next().getValue();
                    list2 = listOfMap.get(i + 1 + k).entrySet().iterator().next().getValue();
                }

                StackPane state = createStateFromGraph(mxCell, x, y, textToSet);
                createPopUp(state, getListViewItemsAsString(list1, list2, state));

            }
        }

        createDashedTransition(initialStackPane, Objects.requireNonNull(findStateByLabel(statesList.get(0))), true);

        for (int i = 0; i < expansionStates.size(); i++) {
            String fromIndex = expansionStates.get(i);
            String toIndex = statesList.get(3 * (i + 1));

            StackPane fromState = findStateByLabel(fromIndex);
            StackPane toState = findStateByLabel(toIndex);

            if (fromState != null && toState != null) {
                createDashedTransition(fromState, toState, false);
            }
        }
    }

    private StackPane findStateByLabel(String label) {
        for (Node node : stateMachinePane.getChildren()) {
            if (node instanceof StackPane stackPane) {
                for (Node child : stackPane.getChildren()) {
                    if (child instanceof Text textNode) {
                        if (textNode.textProperty().get().equals(label)) {
                            return stackPane;
                        }
                    }
                }
            }
        }
        return null;
    }

    private void createPopUp(StackPane stackPane, String listView) {
        Popup popup = new Popup();

        VBox popupContent = new VBox();
        popupContent.setStyle("-fx-background-color: #93E8EB; -fx-padding: 10px; -fx-border-color: black; -fx-border-width: 1px;");
        popupContent.getChildren().add(new Label(listView));

        popup.getContent().add(popupContent);

        stackPane.addEventHandler(MouseEvent.MOUSE_ENTERED, event -> popup.show(stateMachinePane.getScene().getWindow(), event.getScreenX() + 20, event.getScreenY()));

        stackPane.addEventHandler(MouseEvent.MOUSE_EXITED, event -> popup.hide());
    }

    private void createDashedTransition(StackPane fromState, StackPane toState, boolean isInitial) {
        double fromX = fromState.getLayoutX() + 50;
        double fromY = fromState.getLayoutY() + 60;
        double toX = toState.getLayoutX() + 50;
        double toY = toState.getLayoutY();

        if (isInitial) {
            fromX -= 50;
            fromY -= 60;
        }

        Line transition = new Line();
        transition.setStartX(fromX);
        transition.setStartY(fromY);
        transition.setEndX(toX);
        transition.setEndY(toY);

        transition.setStroke(Color.BLACK);
        transition.setStrokeWidth(3);
        transition.setStrokeType(StrokeType.CENTERED);
        transition.getStrokeDashArray().addAll(10d, 5d);

        Polygon arrowhead = createArrowhead(toX, toY, fromX, fromY, false);

        stateMachinePane.getChildren().addAll(transition, arrowhead);
    }

    private Polygon createArrowhead(double toX, double toY, double fromX, double fromY, boolean isSelfTransition) {
        Polygon arrowhead = new Polygon();
        arrowhead.setFill(Color.BLACK);

        if (isSelfTransition) {
            double arrowLength = 13;
            double angle = Math.toRadians(225);

            double x1 = toX - arrowLength * Math.cos(angle);
            double y1 = toY - arrowLength * Math.sin(angle);
            double x2 = toX - arrowLength * Math.cos(angle + Math.PI / 2);
            double y2 = toY - arrowLength * Math.sin(angle + Math.PI / 2);

            arrowhead.getPoints().addAll(toX, toY, x1, y1, x2, y2);
        } else {
            double angle = Math.atan2(toY - fromY, toX - fromX);
            double arrowLength = 13;

            double x1 = toX - arrowLength * Math.cos(angle - Math.toRadians(30));
            double y1 = toY - arrowLength * Math.sin(angle - Math.toRadians(30));
            double x2 = toX - arrowLength * Math.cos(angle + Math.toRadians(30));
            double y2 = toY - arrowLength * Math.sin(angle + Math.toRadians(30));

            arrowhead.getPoints().addAll(toX, toY, x1, y1, x2, y2);
        }

        return arrowhead;
    }

    private void handleStateClick(StackPane state) {
        stateTrack.add(state);

        if (sourceState == null) {
            sourceState = state;
        } else {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Transition Name");
            dialog.setHeaderText(null);
            dialog.setContentText("Enter the name for the transition:");

            Optional<String> result = dialog.showAndWait();
            result.ifPresent(transitionName -> createTransition(sourceState, state, transitionName));


            sourceState = null;
        }
    }

    /**
     * Creates state with necessary features.
     */
    private StackPane createStateFromGraph(mxCell cell, double x, double y, String label) {
        StackPane stackPane = new StackPane();
        double relativeX;
        double relativeY;

        if (levelCount % 3 == 1) {
            relativeX = x - stateMachinePane.getLayoutX();
            relativeY = y - stateMachinePane.getLayoutY() + 80;
        } else {
            relativeX = x - stateMachinePane.getLayoutX();
            relativeY = y - stateMachinePane.getLayoutY();
        }

        stackPane.setLayoutX(relativeX);
        stackPane.setLayoutY(relativeY);

        mxGeometry geometry = cell.getGeometry();
        Rectangle state = new Rectangle(geometry.getWidth(), geometry.getHeight());
        state.setX(relativeX);
        state.setY(relativeY);
        state.setFill(Color.LIGHTBLUE);
        state.setStrokeWidth(1);
        state.setArcHeight(20);
        state.setArcWidth(20);
        state.setStroke(Color.BLACK);

        stackPane.setOnMouseClicked(event -> handleStateClick((StackPane) event.getSource()));
        stackPane.setOnMouseEntered(event -> {
            state.setFill(Color.LIGHTGREEN);
            state.getScene().setCursor(Cursor.HAND);

        });
        stackPane.setOnMouseExited(event -> {
            state.setFill(Color.LIGHTBLUE);
            state.getScene().setCursor(Cursor.DEFAULT);
        });



        Text textNode = new Text(label);

        double textX = relativeX + (geometry.getWidth() - textNode.getBoundsInLocal().getWidth()) / 2;
        double textY = relativeY + (geometry.getHeight() - textNode.getBoundsInLocal().getHeight()) / 2;

        textNode.setX(textX);
        textNode.setY(textY);

        stackPane.getChildren().addAll(state, textNode);
        stateMachinePane.getChildren().add(stackPane);

        this.x += geometry.getWidth() + 200;

        levelCount++;

        if (levelCount % 3 == 0) {
            this.y += geometry.getHeight() + 250;
            this.x = 100;
        }

        return stackPane;
    }

    private String getListViewItemsAsString(ListView<String> listView, ListView<String> listView2, StackPane stackPane) {
        List<String> valuesList1 = new ArrayList<>();
        List<String> valuesList2 = new ArrayList<>();

        if (listView != null && listView2 != null) {
            StringBuilder itemsText = new StringBuilder();
            for (String item : listView.getItems()) {
                String[] split = item.split("::");
                String valueForVariable;
                switch (split[1].trim()) {
                    case "true" -> valueForVariable = "false";
                    case "false" -> valueForVariable = "true";
                    case ">=0" -> valueForVariable = "<0";
                    case "<=0" -> valueForVariable = ">0";
                    case "=0" -> valueForVariable = "!=0";
                    case "!=0" -> valueForVariable = "=0";
                    default -> valueForVariable = "";
                }
                valuesList1.add(split[0] + " :: " + valueForVariable);
                itemsText.append(split[0].trim()).append(" :: ").append(valueForVariable).append("\n");
            }

            for (String item : listView2.getItems()) {
                String[] split = item.split("::");
                String valueForVariable;
                switch (split[1].trim()) {
                    case "true" -> valueForVariable = "false";
                    case "false" -> valueForVariable = "true";
                    case ">=0" -> valueForVariable = "<0";
                    case "<=0" -> valueForVariable = ">0";
                    case "=0" -> valueForVariable = "!=0";
                    case "!=0" -> valueForVariable = "=0";
                    default -> valueForVariable = "";
                }
                valuesList2.add(split[0] + " :: " + valueForVariable);
                itemsText.append(split[0].trim()).append(" :: ").append(valueForVariable).append("\n");
            }
            valuesList1.addAll(valuesList2);
            variablesMap.put(stackPane, valuesList1);
            return itemsText.toString();
        } else {
            return "";
        }
    }

    private void createTransition(StackPane fromState, StackPane toState, String transitionName) {
        Line transition = new Line();
        Line helpTransition1 = new Line();
        Line helpTransition2 = new Line();

        if (fromState == toState) {
            double fromX = fromState.getLayoutX() + fromState.getWidth() / 2;
            double fromY = fromState.getLayoutY() + fromState.getHeight();

            transition.setStartX(fromX);
            transition.setStartY(fromY);
            transition.setEndX(fromX);
            transition.setEndY(fromY + 40);

            helpTransition1.setStartX(fromX);
            helpTransition1.setStartY(fromY + 40);
            helpTransition1.setEndX(fromX - 30);
            helpTransition1.setEndY(fromY + 40);

            helpTransition2.setStartX(fromX - 30);
            helpTransition2.setStartY(fromY + 40);
            helpTransition2.setEndX(fromX - 30);
            helpTransition2.setEndY(fromY + 5);

            transition.setStroke(Color.BLACK);
            transition.setStrokeWidth(3);
            helpTransition1.setStroke(Color.BLACK);
            helpTransition1.setStrokeWidth(3);
            helpTransition2.setStroke(Color.BLACK);
            helpTransition2.setStrokeWidth(3);

            Polygon arrowhead = createArrowhead(fromX - 30, fromY, fromX, fromY, true);

            double labelX = fromX + 5;
            double labelY = fromY + 20;

            Text transitionLabel = new Text(labelX, labelY, transitionName);
            transitionLabel.setFill(Color.RED);


            stateMachinePane.getChildren().addAll(transition, helpTransition2, helpTransition1, arrowhead, transitionLabel);

        } else {
            double fromX;
            double fromY;
            double toX;
            double toY;
            Polygon arrowhead;

            // 1 to 2
            if (fromState.getLayoutX() == 100 && toState.getLayoutX() == 400) {
                fromX = fromState.getLayoutX() + fromState.getWidth();
                fromY = fromState.getLayoutY() + fromState.getHeight();
                toX = toState.getLayoutX();
                toY = toState.getLayoutY() + toState.getHeight() / 2;
                arrowhead = createArrowhead(toX, toY, fromX, fromY, false);
            }
            // 1 to 3
            else if (fromState.getLayoutX() == 100 && toState.getLayoutX() == 700) {
                fromX = fromState.getLayoutX() + fromState.getWidth();
                fromY = fromState.getLayoutY() + fromState.getHeight() / 2;
                toX = toState.getLayoutX();
                toY = toState.getLayoutY() + toState.getHeight() / 2;
                arrowhead = createArrowhead(toX, toY, fromX, fromY, false);
            }
            // 2 to 1
            else if (fromState.getLayoutX() == 400 && toState.getLayoutX() == 100) {
                fromX = fromState.getLayoutX();
                fromY = fromState.getLayoutY();
                toX = toState.getLayoutX() + toState.getWidth();
                toY = toState.getLayoutY() + toState.getHeight() / 1.5;
                arrowhead = createArrowhead(toX, toY, fromX, fromY, false);
            }
            // 2 to 3
            else if (fromState.getLayoutX() == 400 && toState.getLayoutX() == 700) {
                fromX = fromState.getLayoutX() + fromState.getWidth();
                fromY = fromState.getLayoutY();
                toX = toState.getLayoutX();
                toY = toState.getLayoutY() + toState.getHeight() / 1.5;
                arrowhead = createArrowhead(toX, toY, fromX, fromY, false);
            }
            // 3 to 1
            else if (fromState.getLayoutX() == 700 && toState.getLayoutX() == 100) {
                fromX = fromState.getLayoutX();
                fromY = fromState.getLayoutY();
                toX = toState.getLayoutX() + toState.getWidth();
                toY = toState.getLayoutY();
                arrowhead = createArrowhead(toX, toY, fromX, fromY, false);
            }
            // 3 to 2
            else {
                fromX = fromState.getLayoutX();
                fromY = fromState.getLayoutY() + fromState.getHeight();
                toX = toState.getLayoutX() + toState.getWidth();
                toY = toState.getLayoutY() + toState.getHeight() / 2;
                arrowhead = createArrowhead(toX, toY, fromX, fromY, false);
            }

            transition.setStartX(fromX);
            transition.setStartY(fromY);
            transition.setEndX(toX);
            transition.setEndY(toY);

            transition.setStroke(Color.BLACK);
            transition.setStrokeWidth(3);


            double labelX = (fromX + toX) / 2;
            double labelY = (fromY + toY) / 2 - 5;

            Text transitionLabel = new Text(labelX, labelY, transitionName);
            transitionLabel.setFill(Color.RED);

            stateMachinePane.getChildren().addAll(transition, arrowhead, transitionLabel);
        }
    }

    private void setNewVariables(StackPane state) {
        List<String> newList = new ArrayList<>();
        List<String> listString = variablesMap.get(state);
        String valueForVariable = "";
        StringBuilder sb = new StringBuilder();

        for (String s : listString) {
            String[] split = s.split("::");
            switch (split[1].trim()) {
                case "true" -> valueForVariable = "false";
                case "false" -> valueForVariable = "true";
                case ">=0" -> valueForVariable = "<0";
                case "<=0" -> valueForVariable = ">0";
                case "<0" -> valueForVariable = ">=0";
                case ">0" -> valueForVariable = "<=0";
                case "=0" -> valueForVariable = "!=0";
                case "!=0" -> valueForVariable = "=0";
                default -> valueForVariable = "";
            }
            newList.add(split[0].concat(" :: " ).concat(valueForVariable));
            sb.append(split[0]).append(" :: ").append(valueForVariable).append("\n");
        }
        variablesMap.put(state, newList);
        createPopUp(state, sb.toString());

    }


    /**
     * Jumps over states in user-defined order, based on transitions. On current state it changes values of state variables.
     */
    public void triggerTransition(ActionEvent event) {
        Set<StackPane> uniqueClickedStatesSet = new HashSet<>();

        stateTrack.removeIf(state -> !uniqueClickedStatesSet.add(state));

        if (currentTransitionIndex < stateTrack.size()) {
            System.out.println("Clicked State: " + stateTrack.get(currentTransitionIndex));
            System.out.println("Index: " + currentTransitionIndex);
            if(currentTransitionIndex > 0)
                System.out.println("prev state: " + stateTrack.get(currentTransitionIndex - 1));

            if(currentTransitionIndex > 0) {
                setNewVariables(stateTrack.get(currentTransitionIndex - 1));
            }

            setNewVariables(stateTrack.get(currentTransitionIndex));

            StackPane state = stateTrack.get(currentTransitionIndex);
            Timeline timeline = new Timeline();

            KeyValue keyValueRed = new KeyValue(((Rectangle) state.getChildren().get(0)).fillProperty(), Color.RED);
            KeyFrame keyFrameRed = new KeyFrame(Duration.ZERO, keyValueRed);

            timeline.getKeyFrames().add(keyFrameRed);

            KeyValue keyValueOriginal = new KeyValue(((Rectangle) state.getChildren().get(0)).fillProperty(), Color.LIGHTBLUE);
            KeyFrame keyFrameOriginal = new KeyFrame(Duration.seconds(5), keyValueOriginal);

            timeline.getKeyFrames().add(keyFrameOriginal);

            timeline.play();

            currentTransitionIndex++;
        } else {
            System.out.println("No more states to transition.");
            setNewVariables(stateTrack.get(currentTransitionIndex - 1));
            currentTransitionIndex = 0;
        }


    }

    public void handleReset(ActionEvent event) {
        stateMachinePane.getChildren().removeIf(node -> {
            if (node instanceof Line line) {
                return line.getStrokeDashArray().isEmpty();
            } else if (node instanceof Polygon polygon) {
                Node nextNode = stateMachinePane.getChildren().get(stateMachinePane.getChildren().indexOf(polygon));
                return nextNode instanceof Line && ((Line) nextNode).getStrokeDashArray().isEmpty();
            } else return node instanceof Text;
        });

        stateTrack.clear();
        currentTransitionIndex = 0;
    }
}