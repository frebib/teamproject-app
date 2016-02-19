package d2.teamproject.gui;

import d2.teamproject.PARTH;
import d2.teamproject.module.BaseView;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

public abstract class VisualisationView implements BaseView {
    private final Scene scene;
    private final BorderPane frontPane;
    protected final HBox topBox, bottomBox;
    protected final StackPane contentBox, backPane;
    protected final Button backButton, helpButton;

    public VisualisationView() {
        frontPane = new BorderPane();
        frontPane.setPadding(new Insets(10, 20, 10, 20));
        backPane = new StackPane();

        topBox     = new HBox();
        bottomBox  = new HBox();
        contentBox = new StackPane();

        /* Back button implementation - Used to go back to the main menu */
        backButton = new Button("BACK");
        backButton.setAlignment(Pos.CENTER_LEFT);
        backButton.addEventHandler(ActionEvent.ACTION, e -> {
            getController().onClose();
            PARTH.getInstance().getMenu().setVisible();
        });
        topBox.getChildren().add(backButton);

        /* Help button implementation - Used to initialise the tutorial mode  */
        helpButton = new Button("HELP");
        helpButton.setAlignment(Pos.CENTER_LEFT);
        bottomBox.getChildren().add(helpButton);

        frontPane.setTop(topBox);
        frontPane.setCenter(contentBox);
        frontPane.setBottom(bottomBox);

        scene = new Scene(new StackPane(backPane, frontPane), PARTH.MIN_WIDTH, PARTH.MIN_HEIGHT);
    }

    @Override
    public Scene getScene() {
        return scene;
    }
}
