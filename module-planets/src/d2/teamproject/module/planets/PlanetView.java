package d2.teamproject.module.planets;

import d2.teamproject.module.BaseModule;
import d2.teamproject.module.BaseView;
import javafx.scene.layout.StackPane;

public class PlanetView implements BaseView {
    private PlanetModule module;
    private StackPane stackPane;

    public PlanetView(PlanetModule module) {
        this.module = module;
        this.stackPane = new StackPane();
    }

    @Override
    public BaseModule getModule() {
        return module;
    }

    @Override
    public StackPane getPane() {
        /* ADD CODE HERE */

        return stackPane;
    }
}