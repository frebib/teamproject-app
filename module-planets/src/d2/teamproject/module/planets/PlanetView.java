package d2.teamproject.module.planets;

import d2.teamproject.algorithm.sorting.PartitionSortState;
import d2.teamproject.algorithm.sorting.CompareSortState;
import d2.teamproject.algorithm.sorting.ListSortState;
import d2.teamproject.algorithm.sorting.SortState;
import d2.teamproject.gui.VisualisationView;
import d2.teamproject.module.BaseController;
import d2.teamproject.module.planets.gfx.SolarSystem;
import javafx.animation.Transition;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;

import java.util.Map;

public class PlanetView extends VisualisationView {
    public enum AnimState {
        NOTHING,
        COMPARING,
        SWAPPING,
        ZOOMING,
        ZOOMED
    }

    private PlanetController controller;

    private SolarSystem sSystem;
    protected Transition current;
    private AnimState animState = AnimState.NOTHING;

    public PlanetView(PlanetController controller) {
        this.controller = controller;
    }

    public BaseController getController() {
        return controller;
    }

    public Pane getPane() {
        return contentBox;
    }

    public Parent getWindow() {
        return contentBox.getParent().getParent();
    }

    @Override
    public void loadResources(Map<String, Object> res) {
        // Load skybox image
        Image skybox = (Image) res.get("skybox");
        sSystem = new SolarSystem(controller.getPlanets(), skybox);
        contentBox.getChildren().add(sSystem.getScene());
    }

    public void updateState(SortState<Planet> state) {
        // TODO: Handle user input from buttons & tutorial mode and interject animations etc
        // TODO: Implement animations for PartitionSortState

        if (state instanceof CompareSortState) {
            CompareSortState<Planet> csstate = (CompareSortState<Planet>) state;
            animState = AnimState.COMPARING;
            current = sSystem.transitionCompare(csstate, false);
            current.setOnFinished(e -> {
                // Prevent this from looping
                current.setOnFinished(null);

                if (csstate.isSwap()) {
                    animState = AnimState.SWAPPING;
                    current = sSystem.makeSwapTransition(csstate);
                    current.setOnFinished(ev -> {
                        animState = AnimState.NOTHING;

                        // Request the liststate and update the SolarSystem list
                        controller.handleNextState(nState -> {
                            if (!(nState instanceof ListSortState))
                                System.out.println("Something is probably wrong here");
                            sSystem.setPlanetOrder(nState.getList());
                        });
                    });
                    current.playFromStart();
                } else {
                    // Reversing the animation doesn't work
                    // properly, it's likely a bug in the JDK
                    current = sSystem.transitionCompare(csstate, true);
                    current.setOnFinished(ev -> animState = AnimState.NOTHING);
                    current.playFromStart();
                }
            });
            current.playFromStart();
        }
    }

    public AnimState getAnimationState() {
        return animState;
    }
}