package d2.teamproject.visualisations.planets;

import d2.teamproject.visualisations.BaseModule;
import d2.teamproject.visualisations.BaseView;
import javafx.scene.layout.StackPane;

public class PlanetView implements BaseView {
    private PlanetModule module;
    private StackPane stackPane;

    public PlanetView(PlanetModule module) {
        this.module = module;
    }

    @Override
    public BaseModule getModule() {
        return (BaseModule) module;
    }

    public StackPane getStackPane() {
        /* ADD CODE HERE */

        return stackPane;
    }
}