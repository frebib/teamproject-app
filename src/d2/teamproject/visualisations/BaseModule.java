package d2.teamproject.visualisations;

import javafx.scene.image.Image;

public interface BaseModule {
    String getName();
    String getDesc();
    Image getBanner();
    BaseView getView();
}