package d2.teamproject.module;

import d2.teamproject.VisApp;
import javafx.scene.image.Image;

public interface BaseModule {
    void init(VisApp app);
    String getName();
    String getDesc();
    Image getBanner();

    // TODO: Set up GUI redirection here
}