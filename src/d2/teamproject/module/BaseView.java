package d2.teamproject.module;

import javafx.scene.Scene;

import java.util.Map;

public interface BaseView {
    BaseController getController();
    Scene getScene();

    void loadResources(Map<String, Object> res);
}
