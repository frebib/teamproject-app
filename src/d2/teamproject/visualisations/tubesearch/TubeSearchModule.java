package d2.teamproject.visualisations.tubesearch;

import d2.teamproject.visualisations.BaseModule;
import d2.teamproject.visualisations.BaseView;
import javafx.scene.image.Image;

public class TubeSearchModule implements BaseModule {
    @Override
    public void init() {
    }

    @Override
    public String getName() {
        return getClass().getName();
    }

    @Override
    public String getDesc() {
        return null;
    }

    @Override
    public Image getBanner() {
        return null;
    }

    @Override
    public BaseView getGUI() {
        return null;
    }
}