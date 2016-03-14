package d2.teamproject.module.tubesearch;

import d2.teamproject.PARTH;
import d2.teamproject.gui.VisualisationView;
import d2.teamproject.module.BaseController;
import javafx.scene.layout.Pane;

import java.util.Map;

/**
 * @author Parth Chandratreya
 */
public class TubeMapView extends VisualisationView {
    private final TubeSearchController controller;
    private TubeSearchView tubeSearchView;

   public TubeMapView(TubeSearchController controller){
       this.controller = controller;
       topBox.setPrefHeight(1000* 0.05);
       bottomBox.setPrefHeight(1000 * 0.05);
   }

    @Override
    public BaseController getController() {
        return controller;
    }

    @Override
    public void loadResources(Map<String, Object> res) {

    }

    @Override
    public void onOpen() {
        tubeSearchView = new TubeSearchView(controller,(int)(1000*1.25), (int)(750*1.25));
        tubeSearchView.initialise();
        double width = contentBox.getWidth();
        double height = contentBox.getHeight();

        contentBox.getChildren().add(tubeSearchView.getSubScene());

        contentBox.setMinHeight(height);
        contentBox.setMinWidth(width);

        contentBox.setPrefHeight(height);
        contentBox.setPrefWidth(width);

        contentBox.setMaxHeight(height);
        contentBox.setMaxWidth(width);
    }
}
