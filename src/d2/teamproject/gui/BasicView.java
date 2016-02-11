package d2.teamproject.gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

public class BasicView {

    Button backButton;
    StackPane pane;

    public Scene BasicScene() {
        pane = new StackPane();

        /* Border pane implementation - Holds all the diffrent sections */
        BorderPane bp = new BorderPane();
        bp.setPadding(new Insets(10, 20, 10, 20));

        /* Back button implementation - Used to go back to the main menu */
        Button backButton = new Button("BACK");
        HBox topBox = new HBox();
        topBox.getChildren().addAll(backButton);

        /* Center pane implementation - Used to visualise the algorithms */
        StackPane centerPane = new StackPane();
        centerPane.setStyle("-fx-background-color: green");
        /* CALL SPECIFIC CLASS HERE */

        /* Help button implementation - Used to initialise the tutorial mode  */
        Button helpButton = new Button("HELP");
        HBox helpButtonBox = new HBox();
        helpButtonBox.getChildren().addAll(helpButton);
        helpButtonBox.alignmentProperty().setValue(Pos.CENTER_LEFT);

        /* Tick button implementation - Use to check the users' work */
        Button tickButton = new Button("TICK");
        HBox tickButtonBox = new HBox();
        tickButtonBox.getChildren().addAll(tickButton);
        tickButtonBox.alignmentProperty().setValue(Pos.CENTER_RIGHT);

        /* Pane to hold the bottom gui elements */
        StackPane bottomBox = new StackPane();
        bottomBox.getChildren().addAll(helpButtonBox, tickButtonBox);

        bp.setTop(topBox);
        bp.setCenter(centerPane);
        bp.setBottom(bottomBox);
        pane.getChildren().addAll(bp);
        Scene scene = new Scene(pane);

        return scene;
    }
}
