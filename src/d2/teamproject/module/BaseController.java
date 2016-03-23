package d2.teamproject.module;

import javafx.scene.image.Image;

/**
 * @author Joseph Groocock
 */
public interface BaseController {
    String getName();

    String getDesc();

    Image getBanner();

    BaseView getView();

    default void onOpen() {
    }

    default void onClose() {
    }
}