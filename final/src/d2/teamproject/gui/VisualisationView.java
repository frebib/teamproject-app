package d2.teamproject.gui;

import d2.teamproject.PARTH;
import d2.teamproject.module.BaseView;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/**
 * A recommended GUI framework for use with external modules
 * @author Parth Chandratreya
 * @author Joseph Groocock
 */
public abstract class VisualisationView implements BaseView {
    private final Scene scene;
    protected final BorderPane frontPane;
    protected final HBox topBox, bottomBox;
    protected final StackPane contentBox, backPane;
    protected final Button backButton;
    protected final VBox sideBox;

    public VisualisationView() {
        backPane = new StackPane();
        frontPane = new BorderPane();

        bottomBox = new HBox();
        contentBox = new StackPane();

        /* Back button implementation - Used to go back to the main menu */
        backButton = new Button("↩ Back to Menu");
        backButton.setAlignment(Pos.CENTER_LEFT);
        backButton.addEventHandler(ActionEvent.ACTION, e -> {
            getController().onClose();
            PARTH.getInstance().getMenu().setVisible();
        });
        topBox = new HBox(backButton);

        sideBox = new VBox();

        frontPane.setTop(topBox);
        frontPane.setCenter(contentBox);
        frontPane.setBottom(bottomBox);
        frontPane.setLeft(sideBox);

        scene = new Scene(new StackPane(backPane, frontPane), PARTH.WIDTH, PARTH.HEIGHT);
    }

    @Override
    public Scene getScene() {
        return scene;
    }
}
