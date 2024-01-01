package pl.logicalsquare.IOproject.drawingLogic.fxmlControllers;

import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGeometry;
import com.mxgraph.view.mxGraph;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

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

    public StateMachineController() {
        stateMachine = new Group();
        sourceState = new StackPane();
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
                        System.out.println(textNode.textProperty().get() + " " + label + " " + textNode.textProperty().get().equals(label));
                        if (textNode.textProperty().get().equals(label)) {
                            return stackPane;
                        }
                    }
                }
            }
        }
        return null;
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

    private void createStateFromGraph(mxCell cell, double x, double y, String label, String listView) {
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

        Tooltip tooltip = new Tooltip(listView);
        tooltip.setShowDelay(Duration.millis(1));
        tooltip.setHideDelay(Duration.millis(5));
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
            this.y += geometry.getHeight() + 250;
            this.x = 100;
        }

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
}