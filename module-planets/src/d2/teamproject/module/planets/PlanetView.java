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
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.util.Duration;

import java.util.Arrays;
import java.util.Map;
import java.util.function.BiConsumer;

import static d2.teamproject.PARTH.LOG;
import static d2.teamproject.module.planets.PlanetView.AnimState.*;
import static d2.teamproject.module.planets.gfx.PlanetSort.*;

/**
 * @author Parth Chandratreya
 */
public class PlanetView extends VisualisationView {
    public enum AnimState {
        NOTHING,
        COMPARING,
        SWAPPING,
        PARTITIONING,
        ZOOMING,
        ZOOMED;
    }

    private final PlanetController controller;

    private Button prevBtn, nextBtn;
    private ComboBox<PlanetSort> sortByCbx;
    private ComboBox<SortStream.Sorter<Planet>> sorterCbx;
    private ComboBox<PlanetSort.Dir> dirCbx;

    private Image skybox;

    private SolarSystem sSystem;
    private Transition current;
    private AnimState animState = NOTHING;
    private double globalAnimSpeed = 1;

    private HBox bottomCentre;
    private final TextFlow tutorialText;
    private final Text tutorialTitle;
    private final Text tutorialDesc;

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
        topBox.setAlignment(Pos.CENTER_LEFT);
        topBox.setSpacing(16);

        bottomBox.setPrefHeight(PARTH.HEIGHT * 0.2 - panePad.getTop() - panePad.getBottom());
        bottomBox.setPadding(panePad);
        topBox.setSpacing(16);

        Slider animSlide = new Slider(0.2, 5, 1);
        animSlide.valueProperty().addListener((obs, oldVal, newVal) -> {
            globalAnimSpeed = newVal.doubleValue();
            if (current != null)
                current.setRate(globalAnimSpeed);
        });
        VBox animSlidePane = new VBox(animSlide, new Label("Animation Speed"));
        animSlidePane.setAlignment(Pos.CENTER);

        Slider rotSlide = new Slider(1, 100, 20);
        rotSlide.valueProperty().addListener((obs, oldVal, newVal) -> {
            sSystem.setPlanetRotationSpeed(newVal.doubleValue() / 20);
        });
        VBox rotSlidePane = new VBox(rotSlide, new Label("Planet Rotation Speed"));
        rotSlidePane.setAlignment(Pos.CENTER);

        ToggleButton ascDesToggle = new ToggleButton("Tutorial Mode");

        dirCbx = new ComboBox<>(new ImmutableObservableList<>(Dir.ASCENDING, Dir.DESCENDING));
        dirCbx.setValue(Dir.ASCENDING);

        sortByCbx = new ComboBox<>(new ImmutableObservableList<>(DIAMETER, DIST_TO_SUN, MASS, ROTATE_TIME));
        sorterCbx = new ComboBox<>(new ImmutableObservableList<>(
                new SortStream.Sorter<>(QuickSortStream::new, "Quick Sort"),
                new SortStream.Sorter<>(BubbleSortStream::new, "Bubble Sort")
        ));

        BiConsumer<SortStream.Sorter<Planet>, PlanetSort<?>> onChange = (sorter, sortBy) -> {
            if (sorter == null || sortBy == null || controller.getPlanets() == null)
                return;

            Dir dir = dirCbx.getValue();
            PlanetSort<?> sortByDir = (dir != null) ? sortBy.setDirection(dir) : sortBy;

            LOG.finer("Sorting by %s using %s", sortByDir, sorter);
            SortStream<Planet> stream = sorter.get(controller.getPlanets(), sortByDir);
            stream.initialise();
            controller.setSorter(stream);

            tutorial = controller.getTutorial(stream.getClass().getName());
            // TODO: Provide a more coherent tutorial structure
            //updateText("check");

            if (controller.getPlanets() != null && sSystem != null)
                sSystem.resetTransition(controller.getPlanets()).playFromStart();
        };
        sorterCbx.valueProperty().addListener((a, b, newVal) -> onChange.accept(newVal, sortByCbx.getValue()));
        sortByCbx.valueProperty().addListener((a, b, newVal) -> onChange.accept(sorterCbx.getValue(), newVal));
        dirCbx.valueProperty().addListener((a, b, c) -> onChange.accept(sorterCbx.getValue(), sortByCbx.getValue()));

        double height = bottomBox.getPrefHeight() - panePad.getTop() - panePad.getBottom();
        prevBtn = new Button("Step\nBack");
        nextBtn = new Button("Step\nNext");
        prevBtn.setPrefWidth(height * 2.5 / 3);
        nextBtn.setPrefWidth(height * 2.5 / 3);
        prevBtn.setPrefHeight(height);
        nextBtn.setPrefHeight(height);
        prevBtn.setOnAction(e -> controller.prevState());
        nextBtn.setOnAction(e -> controller.nextState());

        bottomCentre = new HBox();
        HBox bottomLPad = new HBox(), bottomRPad = new HBox();
        HBox.setHgrow(bottomLPad, Priority.ALWAYS);
        HBox.setHgrow(bottomRPad, Priority.ALWAYS);

        HBox spacer = new HBox();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        topBox.getChildren().addAll(spacer, animSlidePane, rotSlidePane, sortByCbx, dirCbx, sorterCbx, ascDesToggle);
        bottomBox.getChildren().addAll(prevBtn, bottomLPad, bottomCentre, bottomRPad, nextBtn);
    }

    @Override
    public void onOpen() {
        animState = NOTHING;
        setNavDisabled(false);

        sSystem = new SolarSystem(this, controller.getPlanets(), PARTH.WIDTH, PARTH.HEIGHT * 0.75, skybox);
        contentBox.getChildren().add(sSystem.getScene());
    }

    @Override
    public void loadResources(Map<String, Object> res) {
        // Load skybox image
        skybox = (Image) res.get("skybox");
        LOG.info("skybox loaded");
        // Load tutorial
//        tutorial = controller.getTutorial(tutorialType);
        // Set spacing and alignment
//        bottomBox.setSpacing(200.0);
//        bottomBox.setAlignment(Pos.CENTER);
        // Set the font size
//        tutorialTitle.setFont(new Font(25));
//        tutorialDesc.setFont(new Font(15));
        // Set the text to initial step
        // Set test wrapping width
        tutorialText.setMaxWidth(500);

        tutorialText.getChildren().addAll(tutorialTitle, tutorialDesc);
        tutorialText.setVisible(tutorialMode);
        bottomCentre.getChildren().addAll(tutorialText);

        sorterCbx.setValue(sorterCbx.getItems().get(0));
        sortByCbx.setValue(sortByCbx.getItems().get(0));
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
        // TODO: Fix tutorial crashing!
        if (true) return;
        tutorialTitle.setText(tutorial.getInstruction(key).getTitle());
        tutorialDesc.setText("\n" + tutorial.getInstruction(key).getDesc());
    }

    public void setNavDisabled(boolean disabled) {
        prevBtn.setDisable(disabled);
        nextBtn.setDisable(disabled);
    }

    /**
     * @param state
     */
    public void updateState(SortState<Planet> state) {
        double duration = 150.0;
        if (state == null) {
            LOG.finer("State is null, doing nothing");
            return;
        }

        if (animState == ZOOMED || animState == ZOOMING) {
            Transition zoom = sSystem.zoomOut();
            EventHandler<ActionEvent> handler = zoom.getOnFinished();
            zoom.setOnFinished(e -> {
                updateState(state);
                handler.handle(e);
            });
            zoom.playFromStart();
            return;
        }
        // TODO: Handle user input from buttons & tutorial mode and interject animations etc
        // TODO: Show comparison/sorting information
        // TODO: [Stretch] Show planet names & info on hover

        Transition t;
        if (state.isComplete())
            setTransition(sSystem.finishTransition(), PARTITIONING);

        LOG.finer("SortState=%s", state.getClass().getName());
        if (state instanceof CompareSortState) {
            updateText("compare");
            CompareSortState<Planet> csstate = (CompareSortState<Planet>) state;

            if (tutorialMode)
                duration = 3000;
            t = new SequentialTransition(
                    sSystem.compareTransition(csstate, false),
                    new PauseTransition(new Duration(duration)));
            t.setOnFinished(e -> {
                if (csstate.isSwap()) {
                    updateText("swap");

                    current = sSystem.swapTransition(csstate);
                    current.setOnFinished(ev -> {
                        // Request the liststate and update the SolarSystem list
                        controller.handleNextState(nState -> {
                            if (!(nState instanceof ListSortState))
                                LOG.warning("Something is probably wrong here");

                            sSystem.setPlanetOrder(nState.getList());
                        });
                    });
                    setTransition(current, SWAPPING);
                } else {
                    // Reversing the animation doesn't work
                    // properly, it's likely a bug in the JDK
                    updateText("notSwap");
                    setTransition(sSystem.compareTransition(csstate, true), SWAPPING);
                }
            });
            setTransition(t, COMPARING);
        } else if (state instanceof PartitionSortState) {
            // TODO: Animate setting pivot planet
            t = sSystem.partitionTransition((PartitionSortState<Planet>) state);
            setTransition(t, PARTITIONING);
        }
    }
    private void setTransition(Transition t, AnimState type) {
        if (t == null)
            return;

        animState = type;
        current = t;
        // Handle any handlers already attached
        EventHandler<ActionEvent> onFinished = current.getOnFinished();
        current.onFinishedProperty().set(e -> {
            animState = NOTHING;
            setNavDisabled(false);

            if (onFinished != null)
                onFinished.handle(e);
        });
        current.setRate(globalAnimSpeed);
        current.playFromStart();
    }

    public AnimState getAnimationState() {
        return animState;
    }

    public void setAnimationState(AnimState animState) {
        this.animState = animState;
    }
}