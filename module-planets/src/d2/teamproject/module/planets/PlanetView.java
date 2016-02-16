package d2.teamproject.module.planets;

import d2.teamproject.module.BaseController;
import d2.teamproject.module.BaseView;
import javafx.scene.Group;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

public class PlanetView implements BaseView {
    private PlanetController controller;
    private BorderPane pane;
    private Group root;

    public PlanetView(PlanetController controller) {
        this.controller = controller;

        this.root = new Group();

        this.pane = new BorderPane(root);
    }

    public BaseController getController() {
        return controller;
    }

    @Override
    public Pane getPane() {
        return pane;
    }
}