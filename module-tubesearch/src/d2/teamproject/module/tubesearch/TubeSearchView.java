package d2.teamproject.module.tubesearch;

import com.eclipsesource.json.JsonObject;
import com.sun.javafx.collections.ImmutableObservableList;
import d2.teamproject.PARTH;
import d2.teamproject.algorithm.search.*;
import d2.teamproject.gui.VisualisationView;
import d2.teamproject.module.BaseController;
import d2.teamproject.tutorial.Tutorial;
import javafx.animation.ParallelTransition;
import javafx.animation.Transition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.util.Map;

import static d2.teamproject.PARTH.LOG;

/**
 * @author Parth Chandratreya
 */
public class TubeSearchView extends VisualisationView {
    private final TubeSearchController controller;
    private TubeMap tubeMap;

    private ComboBox<SearchStream.Searcher<TubeStation>> searcherCbx;

    private final boolean tutorialMode;
    private final Text tutorialDesc;
    private final Text tutorialTitle;
    private final TextFlow tutorialText;
    private final String tutorialType = "search"; // Take this in when switching sorts
    private Tutorial tutorial;
    private Image skybox;

    private double mapAspectRatio;

    /**
     * @param controller
     */
    public TubeSearchView(TubeSearchController controller) {
        this.controller = controller;
        tutorialText = new TextFlow();
        tutorialTitle = new Text();
        tutorialDesc = new Text();
        tutorialMode = true;

        topBox.setStyle("-fx-background-color: #1C3F95");
        bottomBox.setStyle("-fx-background-color: #64BEF6");

        Insets panePad = new Insets(16, 20, 16, 20);
        topBox.setPrefHeight(PARTH.HEIGHT * 0.05 - panePad.getTop() - panePad.getBottom());
        topBox.setPadding(panePad);
        topBox.setAlignment(Pos.CENTER_LEFT);
        topBox.setSpacing(16);

        searcherCbx = new ComboBox<>(new ImmutableObservableList<>(
                new SearchStream.Searcher<>(AStarSearchStream::new, "A-Star Search"),
                new SearchStream.Searcher<>(DijkstraSearchStream::new, "Dijkstra's Algorithm"),
                new SearchStream.Searcher<>(BreadthFirstSearchStream::new, "Breadth-First Search"),
                new SearchStream.Searcher<>(DepthFirstSearchStream::new, "Depth-First Search")
        ));
        searcherCbx.valueProperty().addListener((a, b, newVal) -> {
            controller.setSearcher(newVal);
        });

        HBox spacer = new HBox();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        topBox.getChildren().addAll(spacer, searcherCbx);

        bottomBox.setPrefHeight(PARTH.HEIGHT * 0.2 - panePad.getTop() - panePad.getBottom());
        bottomBox.setPadding(panePad);
        bottomBox.setSpacing(16);

        contentBox.setPrefWidth(PARTH.WIDTH);
        contentBox.setPrefHeight(PARTH.HEIGHT - topBox.getPrefHeight() - bottomBox.getPrefHeight());
//        contentBox.setBackground(new Background(new BackgroundFill(Color.HOTPINK, null, null)));
    }

    @Override
    public void onOpen() {
        tubeMap = new TubeMap(controller, mapAspectRatio, contentBox.getPrefWidth(), contentBox.getPrefHeight());
        tubeMap.initialise();

        tubeMap.setClip(new Rectangle(contentBox.getPrefWidth(), contentBox.getPrefHeight()));
        contentBox.getChildren().add(tubeMap);

        searcherCbx.setValue(searcherCbx.getItems().get(0));
    }

    @Override
    public void loadResources(Map<String, Object> res) {
        // Load skybox image
        skybox = (Image) res.get("skybox");
        LOG.info("skybox loaded");
        // Load tutorial
        tutorial = controller.getTutorial(tutorialType);

        JsonObject metadata = ((JsonObject) res.get("stationinfo")).get("metadata").asObject();
        mapAspectRatio = metadata.getDouble("aspectratio", 0);
        // Set spacing and alignment
//        bottomBox.setSpacing(200.0);
//        bottomBox.setAlignment(Pos.CENTER);
        // Set the font size
//        tutorialTitle.setFont(new Font(25));
//        tutorialDesc.setFont(new Font(15));
        // Set the text to initial step
        updateText("step");
        // Set test wrapping width
//        tutorialText.setMaxWidth(500);

//        tutorialText.getChildren().addAll(tutorialTitle, tutorialDesc);
//        tutorialText.setVisible(tutorialMode);
//        bottomBox.getChildren().addAll(tutorialText);
    }

    public void animateState(SearchState<Node<TubeStation>> state) {
        if (state.isComplete())
            tubeMap.animateFinalPath(state.getPath()).play();
        else {
            Transition frontierTrans = tubeMap.animateFrontier(state.getFrontier());
            Transition visitedTrans = tubeMap.animateVisited(state.getVisited());
            ParallelTransition searchTransition = new ParallelTransition();
            searchTransition.getChildren().addAll(frontierTrans, visitedTrans);
            searchTransition.play();
        }
    }

    /**
     * @param key
     */
    private void updateText(String key) {
        tutorialTitle.setText(tutorial.getInstruction(key).getTitle());
        tutorialDesc.setText("\n" + tutorial.getInstruction(key).getDesc());
    }

    @Override
    public BaseController getController() {
        return controller;
    }

    public Parent getWindow() {
        return frontPane.getParent();
    }
}
