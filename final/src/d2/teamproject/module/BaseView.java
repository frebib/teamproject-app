package d2.teamproject.module;

import javafx.scene.Scene;

import java.util.Map;

/**
 * Provides the structure for a View to be managed by a {@link BaseController}
 * @author Joseph Groocock
 */
public interface BaseView {

    /**
     * @return gets the owning {@link BaseController}
     */
    BaseController getController();

    /**
     * @return gets the {@link Scene} that the view renders to
     */
    Scene getScene();

    /**
     * Loads resources passed from the owning {@link BaseController}
     * @param res resources passed to be loaded
     */
    default void loadResources(Map<String, Object> res) {
    }

    /**
     * Called just before the module is first shown to the user
     */
    default void onOpen() {
    }
}
