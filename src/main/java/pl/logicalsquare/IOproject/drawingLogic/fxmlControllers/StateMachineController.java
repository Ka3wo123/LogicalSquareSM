package pl.logicalsquare.IOproject.drawingLogic.fxmlControllers;

import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGeometry;
import com.mxgraph.view.mxGraph;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class StateMachineController {

    @FXML
    private VBox stateMachinePane;
    @FXML
    private ScrollPane scrollPane;
    private Group stateMachine;
    private double x = 100;
    private double y = 100;
    double rectangleWidth = 100;
    double rectangleHeight = 60;

    private List<Map<TextField, ListView<String>>> listOfMap;

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

    public void drawStateMachine(mxGraph graph, List<Map<TextField, ListView<String>>> listOfMap) {
        Object parent = graph.getDefaultParent();

        for (Map<TextField, ListView<String>> textFieldListViewMap : listOfMap) {
            System.out.println(textFieldListViewMap.entrySet().iterator().next().getKey().getText() + " " + textFieldListViewMap.entrySet().iterator().next().getValue().getItems());
            System.out.println(getListViewItemsAsString(textFieldListViewMap.entrySet().iterator().next().getValue()));
        }

        Circle initialState = new Circle(10);
        initialState.setFill(Color.BLACK);
        initialState.setCenterX(x);
        initialState.setCenterY(y);

        stateMachinePane.getChildren().add(initialState);

        Object[] cells = graph.getChildCells(parent);
        for (int i = 0; i < cells.length / 2; i++) {
            Object cell = cells[i];

            if (cell instanceof mxCell mxCell) {
                String textToSet = mxCell.getValue().toString();
                ListView<String> listView = listOfMap.get(i).entrySet().iterator().next().getValue();

                createStateFromGraph(graph, parent, mxCell, x, y, textToSet, listView);
            }
        }
    }

    private void createStateFromGraph(mxGraph graph, Object parent, mxCell cell, double x, double y, String label, ListView<String> listView) {
        StackPane stackPane = new StackPane();

        stackPane.setLayoutX(x);
        stackPane.setLayoutY(y);

        // Assuming the cell represents a vertex (state)
        mxGeometry geometry = cell.getGeometry();
        Rectangle state = new Rectangle(geometry.getWidth(), geometry.getHeight());
        state.setX(x);
        state.setY(y);
        state.setFill(Color.LIGHTBLUE);
        state.setStrokeWidth(1);
        state.setArcHeight(20);
        state.setArcWidth(20);
        state.setStroke(Color.BLACK);

        Tooltip tooltip = new Tooltip(getListViewItemsAsString(listView));
        Tooltip.install(state, tooltip);

        Text textNode = new Text(label);

        double textX = x + (geometry.getWidth() - textNode.getBoundsInLocal().getWidth()) / 2;
        double textY = y + (geometry.getHeight() - textNode.getBoundsInLocal().getHeight()) / 2;

        textNode.setX(textX);
        textNode.setY(textY);

        stackPane.getChildren().addAll(state, textNode);
        stateMachinePane.getChildren().add(stackPane);
    }

    private Rectangle createState(double x, double y, String label, ListView<String> listView) {
        StackPane stackPane = new StackPane();

        stackPane.setLayoutX(x);
        stackPane.setLayoutY(y);

        Rectangle state = new Rectangle(100, 60);
        state.setX(x);
        state.setY(y);
        state.setFill(Color.LIGHTBLUE);
        state.setStrokeWidth(1);
        state.setArcHeight(20);
        state.setArcWidth(20);
        state.setStroke(Color.BLACK);

        Tooltip tooltip = new Tooltip(getListViewItemsAsString(listView));
        Tooltip.install(state, tooltip);

        Text textNode = new Text(label);

        double textX = x + (rectangleWidth - textNode.getBoundsInLocal().getWidth()) / 2;
        double textY = y + (rectangleHeight - textNode.getBoundsInLocal().getHeight()) / 2;

        textNode.setX(textX);
        textNode.setY(textY);

        stackPane.getChildren().addAll(state, textNode);
        stateMachinePane.getChildren().add(stackPane);

        return state;
    }

    private String getListViewItemsAsString(ListView<String> listView) {
        if (listView != null) {
            StringBuilder itemsText = new StringBuilder("State variables:\n");
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

        // You can add arrowheads or other styling to the transition
        transition.setStroke(Color.BLACK);

        return transition;
    }

    public void setListOfMap(List<Map<TextField, ListView<String>>> listOfMap) {
        this.listOfMap = listOfMap;
    }
}