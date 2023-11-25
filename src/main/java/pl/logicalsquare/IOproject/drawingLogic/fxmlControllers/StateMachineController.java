package pl.logicalsquare.IOproject.drawingLogic.fxmlControllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

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

    public void drawStateMachine(Group spanTree) {

        Circle initialState = new Circle(10);
        initialState.setFill(Color.BLACK);
        initialState.setCenterX(x);
        initialState.setCenterY(y);


        stateMachinePane.getChildren().add(initialState);


        int stateCount = 0;

        for (Node node : spanTree.getChildren()) {
            if(stateCount == 3) {
                stateCount = 0;
            }
            if (node instanceof Text text) {
                String textToSet = text.getText();
                if (stateCount % 3 == 0) {
                    createState(x, y, textToSet);
                } else {
                    createState(x, y + 200, textToSet);
                }
//                x += 200;
                stateCount++;
            }
        }
    }


    private Rectangle createState(double x, double y, String label) {
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

        Tooltip tooltip = new Tooltip("""
                Some variables defining state:
                - speed = false
                - door = true
                """);
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


}
