package d2.teamproject.module.planets;

import d2.teamproject.module.BaseController;
import d2.teamproject.module.BaseView;
import javafx.scene.layout.StackPane;

public class PlanetView implements BaseView {
    private PlanetController controller;
    private StackPane stackPane;

    public PlanetView(PlanetController controller) {
        this.controller = controller;
        this.stackPane = new StackPane();
    }

    public BaseController getController() {
        return controller;
    }

    @Override
    public StackPane getPane() {
        /* ADD CODE HERE */

        return stackPane;
    }
}