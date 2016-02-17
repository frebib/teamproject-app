package d2.teamproject.module.planets;

import d2.teamproject.module.BaseController;
import d2.teamproject.module.BaseView;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

import java.util.LinkedHashMap;
import java.util.Map;

public class PlanetView implements BaseView {
    private PlanetController controller;
    private BorderPane pane;
    private Group root;

    private Map<String, Image> textures;

    public PlanetView(PlanetController controller) {
        this.controller = controller;
        controller.setView(this);

        textures = new LinkedHashMap<>();

        this.root = new Group();
        this.pane = new BorderPane(root);
    }

    public Map<String, Image> getTextures() {
        return textures;
    }

    public BaseController getController() {
        return controller;
    }

    @Override
    public Pane getPane() {
        return pane;
    }
}