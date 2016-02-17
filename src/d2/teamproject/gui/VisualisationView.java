package d2.teamproject.gui;

import d2.teamproject.PARTH;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

public class VisualisationView {
    private final Scene scene;
    private final Pane pane;
    private final BorderPane borderPane;
    private final Pane topBox, bottomBox;
    private final Button backButton;

    public VisualisationView() {
        pane = new StackPane();

        /* Border pane implementation - Holds all the different sections */
        borderPane = new BorderPane();
        borderPane.setPadding(new Insets(10, 20, 10, 20));

        /* Back button implementation - Used to go back to the main menu */
        backButton = new Button("BACK");

        topBox = new HBox();
        topBox.getChildren().addAll(backButton);

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
        bottomBox = new StackPane();
        bottomBox.getChildren().add(helpButtonBox);
//        bottomBox.getChildren().addAll(helpButtonBox, tickButtonBox);

        borderPane.setTop(topBox);
        borderPane.setBottom(bottomBox);
        pane.getChildren().addAll(borderPane);

        scene = new Scene(pane, PARTH.MIN_WIDTH, PARTH.MIN_HEIGHT);
    }

    public Pane getTopBox() {
        return topBox;
    }

    public Pane getBottomBox() {
        return bottomBox;
    }

    public Button getBackButton() {
        return backButton;
    }

    public Scene setContent(Pane content) {
        borderPane.setCenter(content);
        return scene;
    }

    public Scene getScene() {
        return scene;
    }
}
