package d2.teamproject.module.planets;

import com.sun.javafx.collections.ImmutableObservableList;
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
 * @author Gulraj Bariah
 */
public class PlanetView extends VisualisationView {
    private static final double PAUSE_TIME = 200;
    private static final double ANIM_INITVAL = 1;
    private static final double ROT_INITVAL = 20;

    public enum AnimState {
        NOTHING,
        COMPARING,
        COMPARED,
        SWAPPING,
        PARTITIONING,
        ZOOMING,
        ZOOMED;
    }

    private final PlanetController controller;

    private Button prevBtn, nextBtn, playBtn;
    private ComboBox<PlanetSort> sortByCbx;
    private ComboBox<SortStream.Sorter<Planet>> sorterCbx;
    private ComboBox<PlanetSort.Dir> dirCbx;
    private Slider animSlide, rotSlide;

    private Image skybox;

    private SolarSystem sSystem;
    private Transition current;
    private AnimState animState = NOTHING;
    private double globalAnimSpeed = 1;

    private HBox bottomCentre;
    private TextFlow tutorialText;
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
        tutorialMode = true;

        Insets panePad = new Insets(16, 20, 16, 20);
        topBox.setPrefHeight(PARTH.HEIGHT * 0.05 - panePad.getTop() - panePad.getBottom());
        topBox.setPadding(panePad);
        topBox.setAlignment(Pos.CENTER_LEFT);
        topBox.setSpacing(16);

        bottomBox.setPrefHeight(PARTH.HEIGHT * 0.2 - panePad.getTop() - panePad.getBottom());
        bottomBox.setPadding(panePad);
        topBox.setSpacing(16);

        animSlide = new Slider(0.2, 5, ANIM_INITVAL);
        animSlide.valueProperty().addListener((obs, oldVal, newVal) -> {
            globalAnimSpeed = newVal.doubleValue();
            if (current != null)
                current.setRate(globalAnimSpeed);
        });
        VBox animSlidePane = new VBox(animSlide, new Label("Animation Speed"));
        animSlidePane.setAlignment(Pos.CENTER);

        rotSlide = new Slider(1, 100, ROT_INITVAL);
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

            loadTutorial(stream.getClass().getName());

            if (controller.getPlanets() != null && sSystem != null)
                sSystem.resetTransition(controller.getPlanets()).playFromStart();
        };
        sorterCbx.valueProperty().addListener((a, b, newVal) -> onChange.accept(newVal, sortByCbx.getValue()));
        sortByCbx.valueProperty().addListener((a, b, newVal) -> onChange.accept(sorterCbx.getValue(), newVal));
        dirCbx.valueProperty().addListener((a, b, c) -> onChange.accept(sorterCbx.getValue(), sortByCbx.getValue()));

        prevBtn = new Button("Step\nBack");
        nextBtn = new Button("Step\nNext");
        playBtn = new Button("▶");
        double height = bottomBox.getPrefHeight() - panePad.getTop() - panePad.getBottom();
        double width = height * 2.5 / 3;
        prevBtn.setPrefWidth(width);
        nextBtn.setPrefWidth(width);
        playBtn.setPrefWidth(width);

        prevBtn.setMinWidth(width);
        nextBtn.setMinWidth(width);
        playBtn.setMinWidth(width);

        prevBtn.setPrefHeight(height);
        nextBtn.setPrefHeight(height);
        playBtn.setPrefHeight(height/2);

        prevBtn.setOnAction(e -> controller.prevState());
        nextBtn.setOnAction(e -> controller.nextState());
        playBtn.setOnAction(e -> LOG.info("Play"));

        tutorialTitle.setFont(new Font(25));
        tutorialDesc.setFont(new Font(15));
        tutorialText = new TextFlow(tutorialTitle, tutorialDesc);
        tutorialText.setPadding(new Insets(0, 32, 0, 32));

        bottomCentre = new HBox(tutorialText);
        HBox bottomLPad = new HBox(), bottomRPad = new HBox();
        HBox.setHgrow(bottomLPad, Priority.ALWAYS);
        HBox.setHgrow(bottomRPad, Priority.ALWAYS);

        HBox spacer = new HBox();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        topBox.getChildren().addAll(spacer,playBtn, animSlidePane, rotSlidePane, sortByCbx, dirCbx, sorterCbx, ascDesToggle);
        bottomBox.getChildren().addAll(prevBtn, bottomLPad, bottomCentre, bottomRPad, nextBtn);
    }

    @Override
    public void onOpen() {
        animState = NOTHING;
        updateNavButtons();
        updateText("information");

        sSystem = new SolarSystem(this, controller.getPlanets(), PARTH.WIDTH, PARTH.HEIGHT * 0.75, skybox);
        contentBox.getChildren().add(sSystem.getScene());

        sorterCbx.setValue(sorterCbx.getItems().get(0));
        sortByCbx.setValue(sortByCbx.getItems().get(0));
        dirCbx.setValue(Dir.ASCENDING);
        animSlide.setValue(ANIM_INITVAL);
        rotSlide.setValue(ROT_INITVAL);
    }

    @Override
    public void loadResources(Map<String, Object> res) {
        // Load skybox image
        skybox = (Image) res.get("skybox");
        LOG.info("skybox loaded");

        sorterCbx.setValue(sorterCbx.getItems().get(0));
        sortByCbx.setValue(sortByCbx.getItems().get(0));
    }

    public void loadTutorial(String tutorialType) {
        tutorial = controller.getTutorial(tutorialType);
        updateText("information");
        LOG.info("Loaded tutorial %s", tutorialType);
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

    private void updateText(String key) {
        updateText(key, 0);
    }
    private void updateText(String key, int titleArgs, Object... args) {
        Object[] descrArgs = Arrays.copyOfRange(args, titleArgs, args.length);
        String title = String.format(tutorial.getInstruction(key).getTitle(), args);
        String descr = String.format(tutorial.getInstruction(key).getDesc(), descrArgs);
        tutorialTitle.setText(title);
        tutorialDesc.setText("\n" + descr);
    }

    public void updateNavButtons() {
        prevBtn.setDisable(!controller.getSorter().hasPrevious());
        nextBtn.setDisable(!controller.getSorter().hasNext());
    }

    public void disableNavButtons(boolean disabled) {
        prevBtn.setDisable(disabled);
        nextBtn.setDisable(disabled);
    }

    public void stepForward() {
        Transition t;       // A transition variable (y) :D

        /* If in a partial compare state */
        if (animState == COMPARED) {
            CompareSortState<Planet> state = (CompareSortState<Planet>) controller.getSorter().getCurrent();
            Planet p1 = controller.getPlanets().get((int) state.getCompares().getX());
            Planet p2 = controller.getPlanets().get((int) state.getCompares().getY());
            if (state.isSwap()) {

                t = sSystem.swapTransition(state);
                t.setOnFinished(ev -> {
                    // Request the liststate and update the SolarSystem list
                    SortState<Planet> nState = controller.getSorter().getNext();
                    if (!(nState instanceof ListSortState))
                        LOG.warning("Something is probably wrong here");

                    sSystem.setPlanetOrder(nState.getList());
                });
                updateText("information");
                doTransition(t, SWAPPING);
            } else {
                // Reversing the animation doesn't work
                // properly, it's likely a bug in the JDK
                updateText("information");
                doTransition(sSystem.compareTransition(state, true), SWAPPING);
            }
            return;
        }

        // No more states to handle
        if (!controller.getSorter().hasNext()) {
            LOG.info("All sort states exhausted");
            return;
        }

        /* Handle a new state */
        SortState<Planet> state = controller.getSorter().getNext();
        LOG.finer("SortState=%s", state);

        // This shouldn't happen, if it does then something is broken
        if (state == null) LOG.finer("State is null, doing nothing");

        /* Zoom out if already zoomed in */
        else if (animState == ZOOMED || animState == ZOOMING) {
            t = sSystem.zoomOut();
            EventHandler<ActionEvent> handler = t.getOnFinished();
            t.setOnFinished(e -> {
                stepForward();
                handler.handle(e);
            });
            t.playFromStart();

        } else if (state.isComplete()) {
            updateText("done");
            doTransition(sSystem.finishTransition(), PARTITIONING);
        } else if (state instanceof CompareSortState) {
            CompareSortState<Planet> csstate = (CompareSortState<Planet>) state;

            Planet p1 = controller.getPlanets().get((int) csstate.getCompares().getX());
            Planet p2= controller.getPlanets().get((int) csstate.getCompares().getY());

            t = new SequentialTransition(
                    sSystem.compareTransition(csstate, false),
                    new PauseTransition(new Duration(PAUSE_TIME))
            );
            t.setOnFinished(e -> animState = COMPARED);

            if(((int)csstate.getCompares().getX()) > csstate.getPivot() && !csstate.isSwap()) {
                updateText("notSwap", 2, p1.getName(), p2.getName());
            }
            else if (!csstate.isSwap()) {
                updateText("compare",2, p1.getName(), p2.getName());
            }
            else if(((int)csstate.getCompares().getX()) > csstate.getPivot() && csstate.isSwap()) {
                updateText("swap", 2, p1.getName(), p2.getName());
            }
            else if (csstate.isSwap()) {
                updateText("check",2, p1.getName(), p2.getName());
            }
            doTransition(t, COMPARING);
        } else if (state instanceof PartitionSortState) {
            // TODO: Animate setting pivot planet
            updateText("pickPivot",1, controller.getPlanets().get(((PartitionSortState) state).getPivot()).getName());
            t = sSystem.partitionTransition((PartitionSortState<Planet>) state);
            doTransition(t, PARTITIONING);
        }
    }

    public void stepBackward() {
        updateNavButtons();
    }

    private void doTransition(Transition t, AnimState type) {
        if (t == null)
            return;

        animState = type;
        current = t;
        // Handle any handlers already attached
        EventHandler<ActionEvent> onFinished = current.getOnFinished();
        current.onFinishedProperty().set(e -> {
            animState = NOTHING;
            updateNavButtons();

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