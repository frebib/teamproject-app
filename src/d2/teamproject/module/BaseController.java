package d2.teamproject.module;

import javafx.scene.image.Image;

/**
 * Provides a structure to hook the module controller into the application
 * @author Joseph Groocock
 */
public interface BaseController {

    /**
     * @return the name of the module
     */
    String getName();

    /**
     * @return the description of the module
     */
    String getDesc();

    /**
     * @return the banner image of the module to be displayed
     * in the {@link d2.teamproject.gui.MainMenuView}
     */
    Image getBanner();

    /**
     * @return gets the {@link BaseView} object for the module
     */
    BaseView getView();

    /**
     * Called just before the module is first shown to the user
     */
    default void onOpen() {
    }

    /**
     * Called just before the module is closed
     */
    default void onClose() {
    }
}