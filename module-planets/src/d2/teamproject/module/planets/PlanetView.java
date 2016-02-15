package d2.teamproject.module.planets;

import d2.teamproject.module.BaseController;
import d2.teamproject.module.BaseView;
import javafx.scene.layout.StackPane;

public class PlanetView implements BaseView {
    private PlanetController module;
    private StackPane stackPane;

    public PlanetView(PlanetController module) {
        this.module = module;
        this.stackPane = new StackPane();
    }

    @Override
    public BaseController getModule() {
        return module;
    }

    @Override
    public StackPane getPane() {
        /* ADD CODE HERE */

        return stackPane;
    }
}