package d2.teamproject.module.planets;

import com.sun.javafx.collections.ImmutableObservableList;
import com.sun.javafx.collections.ObservableListWrapper;
import d2.teamproject.PARTH;
import d2.teamproject.algorithm.sorting.*;
import d2.teamproject.gui.VisualisationView;
import d2.teamproject.module.BaseController;
import d2.teamproject.module.planets.gfx.PlanetSort;
import d2.teamproject.module.planets.gfx.SolarSystem;
import d2.teamproject.tutorial.Tutorial;
import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.Transition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.util.Duration;

import java.util.Arrays;
import java.util.Map;

import static d2.teamproject.PARTH.LOG;
import static d2.teamproject.module.planets.gfx.PlanetSort.*;

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

    private Button prevBtn, nextBtn;
    private Slider animSlide, rotSlide;
    private ToggleButton ascDesToggle;
    private ComboBox<PlanetSort> sortByCbx;
    private ComboBox<SortStream.Sorter<Planet>> sorterCbx;

    private Image skybox;

    private SolarSystem sSystem;
    private Transition current;
    private AnimState animState = AnimState.NOTHING;
    private double globalAnimSpeed = 1;

    private final TextFlow tutorialText;
    private final Text tutorialTitle;
    private final Text tutorialDesc;

    private final String tutorialType = "bubblesort"; // Take this in when switching sorts
    private Tutorial tutorial;
    private boolean tutorialMode;

    /**
     * @param controller
     */
    public PlanetView(PlanetController controller) {
        this.controller = controller;
        tutorialText = new TextFlow();
        tutorialTitle = new Text();
        tutorialDesc = new Text();
        tutorialMode = false;

        Insets panePad = new Insets(16, 20, 16, 20);
        topBox.setPrefHeight(PARTH.HEIGHT * 0.05 - panePad.getTop() - panePad.getBottom());
        topBox.setPadding(panePad);
        topBox.setAlignment(Pos.CENTER_RIGHT);
        topBox.setSpacing(16);

        bottomBox.setPrefHeight(PARTH.HEIGHT * 0.2 - panePad.getTop() - panePad.getBottom());
        bottomBox.setPadding(panePad);
        topBox.setSpacing(16);

        animSlide = new Slider(0.2, 5, 1);
        animSlide.valueProperty().addListener((obs, oldVal, newVal) -> {
            globalAnimSpeed = newVal.doubleValue();
            if (current != null)
                current.setRate(globalAnimSpeed);
        });
        VBox animSlidePane = new VBox(animSlide, new Label("Animation Speed"));
        animSlidePane.setAlignment(Pos.CENTER);

        rotSlide = new Slider(1, 100, 20);
        rotSlide.valueProperty().addListener((obs, oldVal, newVal) -> {
            sSystem.setPlanetRotationSpeed(newVal.doubleValue() / 20);
        });
        VBox rotSlidePane = new VBox(rotSlide, new Label("Planet Rotation Speed"));
        rotSlidePane.setAlignment(Pos.CENTER);

        ascDesToggle = new ToggleButton("Tutorial Mode");

        sortByCbx = new ComboBox<>(new ImmutableObservableList<>(DIAMETER, DIST_TO_SUN, MASS, ROTATE_TIME));
        sortByCbx.setValue(sortByCbx.getItems().get(0));

        sorterCbx = new ComboBox<>(new ObservableListWrapper<>(Arrays.asList(
                new SortStream.Sorter<>(QuickSortStream::new, "Quick Sort"),
                new SortStream.Sorter<>(BubbleSortStream::new, "Bubble Sort")
        )));
        sorterCbx.valueProperty().addListener((obs, oldVal, newVal) -> {
            SortStream<Planet> sorter = newVal.get(controller.getPlanets(), sortByCbx.getValue());
            controller.setSorter(sorter);
        });
        sorterCbx.setValue(sorterCbx.getItems().get(0));

        prevBtn = new Button("Step\nBack");
        nextBtn = new Button("Step\nNext");
        double height = bottomBox.getPrefHeight() - panePad.getTop() - panePad.getBottom();
//        prevBtn.setPrefWidth(height * (2/3));
        prevBtn.setPrefHeight(height);
//        nextBtn.setPrefWidth(height * (2/3));
        nextBtn.setPrefHeight(height);

        topBox.getChildren().addAll(animSlidePane, rotSlidePane, sortByCbx, sorterCbx, ascDesToggle);
        bottomBox.getChildren().addAll(prevBtn, nextBtn);
    }

    @Override
    public void onOpen() {
        sSystem = new SolarSystem(controller.getPlanets(), PARTH.WIDTH, PARTH.HEIGHT * 0.75, skybox);
        contentBox.getChildren().add(sSystem.getScene());
    }

    @Override
    public void loadResources(Map<String, Object> res) {
        // Load skybox image
        skybox = (Image) res.get("skybox");
        LOG.info("skybox loaded");
        // Load tutorial
        tutorial = controller.getTutorial(tutorialType);
        // Set spacing and alignment
//        bottomBox.setSpacing(200.0);
//        bottomBox.setAlignment(Pos.CENTER);
        // Set the font size
        tutorialTitle.setFont(new Font(25));
        tutorialDesc.setFont(new Font(15));
        // Set the text to initial step
        updateText("check");
        // Set test wrapping width
        tutorialText.setMaxWidth(500);

        tutorialText.getChildren().addAll(tutorialTitle, tutorialDesc);
        tutorialText.setVisible(tutorialMode);
//        bottomBox.getChildren().addAll(tutorialText);
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

    /**
     * @param key
     */
    private void updateText(String key) {
        tutorialTitle.setText(tutorial.getInstruction(key).getTitle());
        tutorialDesc.setText("\n" + tutorial.getInstruction(key).getDesc());
    }

    /**
     * @param state
     */
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
            if (tutorialMode) duration = 3000;
            current = new SequentialTransition(current, new PauseTransition(new Duration(duration)));
            current.setRate(globalAnimSpeed);
            current.setOnFinished(e -> {
                if (csstate.isSwap()) {
                    updateText("swap");
                    animState = AnimState.SWAPPING;
                    current = sSystem.makeSwapTransition(csstate);
                    current.setRate(globalAnimSpeed);
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
                    current.setRate(globalAnimSpeed);
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