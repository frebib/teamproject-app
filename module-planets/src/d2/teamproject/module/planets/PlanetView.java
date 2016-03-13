package d2.teamproject.module.planets;

import d2.teamproject.PARTH;
import d2.teamproject.algorithm.sorting.CompareSortState;
import d2.teamproject.algorithm.sorting.ListSortState;
import d2.teamproject.algorithm.sorting.PartitionSortState;
import d2.teamproject.algorithm.sorting.SortState;
import d2.teamproject.gui.VisualisationView;
import d2.teamproject.module.BaseController;
import d2.teamproject.module.planets.gfx.SolarSystem;
import d2.teamproject.tutorial.Tutorial;
import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.Transition;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.util.Duration;

import java.util.Map;

import static d2.teamproject.PARTH.LOG;

/**
 * @author Parth Chandratreya
 */
public class PlanetView extends VisualisationView {


    public enum AnimState {
        NOTHING,
        COMPARING,
        SWAPPING,
        ZOOMING,
        ZOOMED;
    }
    private final PlanetController controller;

    private Image skybox;

    private SolarSystem sSystem;
    private Transition current;
    private AnimState animState = AnimState.NOTHING;
    private final TextFlow tutorialText;
    private final Text tutorialTitle;
    private final Text tutorialDesc;
    private final String tutorialType = "bubblesort"; // Take this in when switching sorts
    private Tutorial tutorial;
    private boolean tutorialMode;

    public PlanetView(PlanetController controller) {
        this.controller = controller;
        tutorialText = new TextFlow();
        tutorialTitle = new Text();
        tutorialDesc = new Text();
        tutorialMode = false;

        topBox.setPrefHeight(PARTH.HEIGHT * 0.08);
        bottomBox.setPrefHeight(PARTH.HEIGHT * 0.2);
    }

    public BaseController getController() {
        return controller;
    }

    public Pane getPane() {
        return contentBox;
    }

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
        // Load tutorial
        tutorial = controller.getTutorial(tutorialType);
        // Set spacing and alignment
        bottomBox.setSpacing(200.0);
        bottomBox.setAlignment(Pos.CENTER);
        // Set the font size
        tutorialTitle.setFont(new Font(25));
        tutorialDesc.setFont(new Font(15));
        // Set the text to initial step
        updateText("check");
        // Set  test wrapping width
        tutorialText.setMaxWidth(500);

        tutorialText.getChildren().addAll(tutorialTitle,tutorialDesc);
        tutorialText.setVisible(tutorialMode);
        bottomBox.getChildren().addAll(tutorialText);
    }

    private void updateText(String key){
        tutorialTitle.setText(tutorial.getInstruction(key).getTitle());
        tutorialDesc.setText("\n"+tutorial.getInstruction(key).getDesc());
    }

    public void updateState(SortState<Planet> state) {
        double duration = 150.0;
        if (state == null) return;
        // TODO: Handle user input from buttons & tutorial mode and interject animations etc
        // TODO: Fix planet zooming
        // TODO: Show comparison/sorting information
        // TODO: [Stretch] Show planet names & info on hover

        if (state.isComplete())
            sSystem.setFinished();

        LOG.finer("SortState=%s", state);
        if (state instanceof CompareSortState) {
            updateText("compare");
            CompareSortState<Planet> csstate = (CompareSortState<Planet>) state;
            animState = AnimState.COMPARING;
            current = sSystem.transitionCompare(csstate, false);
            if(tutorialMode) duration = 3000;
            current = new SequentialTransition(current, new PauseTransition(new Duration(duration)));
            current.setOnFinished(e -> {
                if (csstate.isSwap()) {
                    updateText("swap");
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
                    updateText("notSwap");
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