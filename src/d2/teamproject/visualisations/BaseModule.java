package d2.teamproject.visualisations;

import javafx.scene.image.Image;

public interface BaseModule {
    void init();
    String getName();
    String getDesc();
    Image getBanner();
    BaseView getGUI();

    // TODO: Set up GUI redirection here
}