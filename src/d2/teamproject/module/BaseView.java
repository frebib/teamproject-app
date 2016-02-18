package d2.teamproject.module;

import d2.teamproject.gui.VisualisationView;
import javafx.scene.Scene;

import java.util.Map;

public interface BaseView {
    BaseController getController();
    Scene getScene(VisualisationView visView);

    void loadResources(Map<String, Object> res);
}
