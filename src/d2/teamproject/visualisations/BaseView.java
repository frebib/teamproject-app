package d2.teamproject.visualisations;

import javafx.scene.layout.StackPane;

public abstract class BaseView {
    private final BaseModule module;

    public BaseView(BaseModule module) {
        this.module = module;
    }

    public BaseModule getModule() {
        return module;
    }

    public abstract StackPane getPane();
}
