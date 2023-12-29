package pl.logicalsquare.IOproject.drawingLogic.fxmlControllers;

import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGeometry;
import com.mxgraph.view.mxGraph;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.util.List;
import java.util.Map;

public class StateMachineController {

    @FXML
    private Pane stateMachinePane;
    @FXML
    private ScrollPane scrollPane;
    private Group stateMachine;
    private double x = 100;
    private double y = 100;
    double rectangleWidth = 100;
    double rectangleHeight = 60;
    @FXML
    private Label scenarioLabel;
    private static int levelCount = 0;

    private List<Map<TextField, ListView<String>>> listOfMap;
    private Circle sourceState;

    public StateMachineController() {
        stateMachine = new Group();
    }

    public void handleOpen(ActionEvent event) {
        System.out.println("Open clicked");
    }

    public void handleExit(ActionEvent event) {
        System.out.println("Exit cliced");
    }

    public void handleSave(ActionEvent event) {
        System.out.println("Save clicked");
    }

    private ListView<String> getListViewForTextField(List<Map<TextField, ListView<String>>> listOfMaps, String textFieldText) {
        for (Map<TextField, ListView<String>> map : listOfMaps) {
            for (TextField textField : map.keySet()) {
                if (textField.getText().equals(textFieldText)) {
                    return map.get(textField);
                }
            }
        }
        return null;
    }

    public void drawStateMachine(mxGraph graph, List<Map<TextField, ListView<String>>> listOfMap, List<String> statesList) {
        Object parent = graph.getDefaultParent();

        StringBuilder scenario = new StringBuilder();

        int statesCount = 0;
        int totalStates = statesList.size();

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


        Circle initialState = new Circle(10);
        initialState.setFill(Color.BLACK);
        initialState.setCenterX(x - stateMachinePane.getLayoutX() + 170);
        initialState.setCenterY(y - stateMachinePane.getLayoutY() - 50);

        initialState.setOnMouseClicked(event -> handleStateClick((Circle) event.getSource()));

        stateMachinePane.getChildren().add(initialState);

        Object[] cells = graph.getChildCells(parent);
        for (int i = 0; i < cells.length; i++) {
            Object cell = cells[i];

            if (cell instanceof mxCell mxCell) {
                ListView<String> list1;
                ListView<String> list2;
                String textToSet = mxCell.getValue().toString();

                if (i % 4 == 0) {
                    list1 = listOfMap.get(i).entrySet().iterator().next().getValue();
                    list2 = listOfMap.get(i + 2).entrySet().iterator().next().getValue();
                } else if (i % 4 == 1) {
                    list1 = listOfMap.get(i + 1).entrySet().iterator().next().getValue();
                    list2 = listOfMap.get(i + 2).entrySet().iterator().next().getValue();
                } else {
                    list1 = listOfMap.get(i - 1).entrySet().iterator().next().getValue();
                    list2 = listOfMap.get(i + 1).entrySet().iterator().next().getValue();
                }

                createStateFromGraph(mxCell, x, y, textToSet, getListViewItemsAsString(list1).concat(getListViewItemsAsString(list2)));
            }
        }
    }

    private void handleStateClick(Circle state) {
        if (sourceState == null) {
            // If no source state is selected, set the clicked state as the source
            sourceState = state;
        } else {
            // If a source state is already selected, create a transition between them
            createTransition(sourceState, state);

            // Reset the source state to null for the next interaction
            sourceState = null;
        }
    }

    private Object createStateFromGraph(mxCell cell, double x, double y, String label, String listView) {
        StackPane stackPane = new StackPane();

        double relativeX = x - stateMachinePane.getLayoutX();
        double relativeY = y - stateMachinePane.getLayoutY();

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

        Tooltip tooltip = new Tooltip(listView);
        tooltip.setShowDelay(Duration.millis(1));
        tooltip.setHideDelay(Duration.seconds(5));
        Tooltip.install(stackPane, tooltip);

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
            this.y += geometry.getHeight() + 100;
            this.x = 100;
        }

        return stackPane;
    }

    private String getListViewItemsAsString(ListView<String> listView) {
        if (listView != null) {
            StringBuilder itemsText = new StringBuilder();
            for (String item : listView.getItems()) {
                itemsText.append("- ").append(item).append("\n");
            }
            return itemsText.toString();
        } else {
            return "";
        }
    }

    private Line createTransition(Circle fromState, Circle toState) {
        Line transition = new Line();
        transition.setStartX(fromState.getCenterX() + fromState.getRadius());
        transition.setStartY(fromState.getCenterY());
        transition.setEndX(toState.getCenterX() - fromState.getRadius());
        transition.setEndY(toState.getCenterY());

        transition.setStroke(Color.BLACK);

        return transition;
    }
}