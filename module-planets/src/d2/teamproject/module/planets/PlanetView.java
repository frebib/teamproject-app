package d2.teamproject.module.planets;

import com.eclipsesource.json.JsonArray;
import d2.teamproject.PARTH;
import d2.teamproject.algorithm.sorting.CompareSortState;
import d2.teamproject.algorithm.sorting.ListSortState;
import d2.teamproject.algorithm.sorting.PartitionSortState;
import d2.teamproject.algorithm.sorting.SortState;
import d2.teamproject.gui.VisualisationView;
import d2.teamproject.module.BaseController;
import d2.teamproject.module.planets.gfx.SolarSystem;
import javafx.animation.Transition;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.Map;

import static d2.teamproject.PARTH.LOG;

public class PlanetView extends VisualisationView {
    public enum AnimState {
        NOTHING,
        COMPARING,
        SWAPPING,
        ZOOMING,
        ZOOMED
    }

    private PlanetController controller;

    private Image skybox;
    private SolarSystem sSystem;
    protected Transition current;
    private AnimState animState = AnimState.NOTHING;
    public JsonArray helpArray;

    public PlanetView(PlanetController controller) {
        this.controller = controller;

        topBox.setPrefHeight(PARTH.HEIGHT * 0.08);
        bottomBox.setPrefHeight(PARTH.HEIGHT * 0.2);

        addTutorial(controller,bottomBox);
    }

    public void addTutorial(PlanetController controller,HBox bottomBox){
//        bottomBox.setStyle("-fx-background-color: red");
        bottomBox.setSpacing(200.0);
        bottomBox.setAlignment(Pos.CENTER);

//        Text text = new Text(controller.getTutorial().getCurrent().getTitle());
        Text text = new Text("Test");
        text.setFont(new Font(30));
        bottomBox.getChildren().add(text);
    }

    public BaseController getController() {
        return controller;
    }

    public Pane getPane() {
        return contentBox;
    }

    public JsonArray getHelp() { return  helpArray;}

    public Parent getWindow() {
        return frontPane.getParent();
    }

    @Override
    public void onOpen() {
        sSystem = new SolarSystem(controller.getPlanets(), (int) PARTH.WIDTH, (int) (PARTH.HEIGHT * 0.75), skybox);
        contentBox.getChildren().add(sSystem.getScene());
    }

    @Override
    public void loadResources(Map<String, Object> res) {
        // Load skybox image
        skybox = (Image) res.get("skybox");
        System.out.println("skybox loaded");
    }

    public void updateState(SortState<Planet> state) {
        if (state == null) return;
        // TODO: Handle user input from buttons & tutorial mode and interject animations etc
        // TODO: Fix planet zooming
        // TODO: Show comparison/sorting information
        // TODO: [Stretch] Show planet names & info on hover

        if (state.isComplete())
            sSystem.setFinished();

        LOG.finer("SortState=%s", state);
        if (state instanceof CompareSortState) {
            CompareSortState<Planet> csstate = (CompareSortState<Planet>) state;
            animState = AnimState.COMPARING;
            current = sSystem.transitionCompare(csstate, false);
            current.setOnFinished(e -> {
                if (csstate.isSwap()) {
                    animState = AnimState.SWAPPING;
                    current = sSystem.makeSwapTransition(csstate);
                    current.setOnFinished(ev -> {
                        animState = AnimState.NOTHING;

                        // Request the liststate and update the SolarSystem list
                        controller.handleNextState(nState -> {
                            if (!(nState instanceof ListSortState))
                                LOG.warning("Something is probably wrong here");

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
        } else if (state instanceof PartitionSortState) {
            // TODO: Animate setting pivot planet
            sSystem.setPartition((PartitionSortState<Planet>) state);
        }
    }

    public AnimState getAnimationState() {
        return animState;
    }
}