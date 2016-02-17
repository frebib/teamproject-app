package d2.teamproject.module;

import javafx.scene.image.Image;

public interface BaseController {
    String getName();
    String getDesc();
    Image getBanner();
    BaseView getView();
    void setView(BaseView view);

    void onOpen();
    void onClose();
}