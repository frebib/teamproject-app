package d2.teamproject.module;

import d2.teamproject.gui.VisualisationView;
import javafx.scene.Scene;

public interface BaseView {
    BaseController getController();
    Scene getScene(VisualisationView visView);
}
