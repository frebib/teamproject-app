package d2.teamproject.module.tubesearch;

import com.eclipsesource.json.JsonObject;
import com.sun.javafx.collections.ImmutableObservableList;
import com.sun.javafx.scene.control.skin.TableColumnHeader;
import d2.teamproject.PARTH;
import d2.teamproject.algorithm.search.*;
import d2.teamproject.algorithm.search.Node;
import d2.teamproject.gui.VisualisationView;
import d2.teamproject.module.BaseController;
import d2.teamproject.module.tubesearch.gfx.TubeMap;
import d2.teamproject.tutorial.Tutorial;
import javafx.animation.ParallelTransition;
import javafx.animation.Transition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;

import static d2.teamproject.PARTH.LOG;

/**
 * @author Parth Chandratreya
 * @author Luke Taher
 */
public class TubeSearchView extends VisualisationView {
    private final TubeSearchController controller;
    private final Rectangle keyImage;
    private TubeMap tubeMap;

    private Button prevBtn, nextBtn, playBtn, begBtn, medBtn, hardBtn;
    private ComboBox<SearchStream.Searcher<TubeStation>> searcherCbx;

    private HBox bottomCentre;
    private TextFlow tutorialText;
    private final Text tutorialTitle;
    private final Text tutorialDesc;

    private Tutorial tutorial;
    private Image skybox;

    private double mapAspectRatio;
    private Image key;

    /**
     * @param controller
     */
    public TubeSearchView(TubeSearchController controller) {
        this.controller = controller;
        tutorialText = new TextFlow();
        tutorialTitle = new Text();
        tutorialDesc = new Text();

        keyImage = new Rectangle(236, 396, Color.AQUA);

        Insets panePad = new Insets(16, 20, 16, 20);
        topBox.setStyle("-fx-background-color: #1C3F95");
        topBox.setPrefHeight(PARTH.HEIGHT * 0.05 - panePad.getTop() - panePad.getBottom());
        topBox.setPadding(panePad);
        topBox.setAlignment(Pos.CENTER_LEFT);
        topBox.setSpacing(16);

        bottomBox.setStyle("-fx-background-color: #64BEF6");
        bottomBox.setPrefHeight(PARTH.HEIGHT * 0.18 - panePad.getTop() - panePad.getBottom());
        bottomBox.setPadding(panePad);
        bottomBox.setSpacing(16);

//        sideBox.setStyle("-fx-background-color: red");
        sideBox.setPrefWidth(236);
        sideBox.setPadding(panePad);
        sideBox.setSpacing(1);
        sideBox.setAlignment(Pos.CENTER);

        searcherCbx = new ComboBox<>(new ImmutableObservableList<>(
                new SearchStream.Searcher<>(AStarSearchStream::new, AStarSearchStream.class, "A-Star Search"),
                new SearchStream.Searcher<>(DijkstraSearchStream::new, DijkstraSearchStream.class, "Dijkstra's Algorithm"),
                new SearchStream.Searcher<>(BreadthFirstSearchStream::new, BreadthFirstSearchStream.class, "Breadth-First Search"),
                new SearchStream.Searcher<>(DepthFirstSearchStream::new, DepthFirstSearchStream.class, "Depth-First Search")
        ));
        searcherCbx.valueProperty().addListener((a, b, newVal) -> controller.setSearcher(newVal));

        prevBtn = new Button("Step\nBack");
        nextBtn = new Button("Step\nNext");
        playBtn = new Button("▶");
        begBtn = new Button("Beginner");
        medBtn = new Button("Medium");
        hardBtn = new Button("Hard");
        double height = bottomBox.getPrefHeight() - panePad.getTop() - panePad.getBottom();
        double width = height * 2.5 / 3;
        prevBtn.setPrefWidth(width);
        nextBtn.setPrefWidth(width);
        playBtn.setPrefWidth(width);
        begBtn.setPrefWidth(width*2);
        medBtn.setPrefWidth(width*2);

        prevBtn.setMinWidth(width);
        nextBtn.setMinWidth(width);
        playBtn.setMinWidth(width);
        begBtn.setMinWidth(width*2);
        medBtn.setMinWidth(width*2);
        hardBtn.setMinWidth(width);

        prevBtn.setPrefHeight(height);
        nextBtn.setPrefHeight(height);
        playBtn.setPrefHeight(height);
        begBtn.setPrefHeight(height);
        medBtn.setPrefHeight(height);
        hardBtn.setPrefHeight(height);

        prevBtn.setOnAction(e -> animateState(controller.prevState()));
        nextBtn.setOnAction(e -> animateState(controller.nextState()));
        playBtn.setOnAction(e -> {
            playBtn.setDisable(true);
            SearchState<Node<TubeStation>> state = controller.nextState();
            if (state.isComplete())
                tubeMap.animateFinalPath(state.getPath()).play();
            else {
                Transition frontierTrans = tubeMap.animateFrontier(state.getFrontier());
                Transition visitedTrans = tubeMap.animateVisited(state.getVisited());
                ParallelTransition searchTransition = new ParallelTransition();
                searchTransition.getChildren().addAll(frontierTrans, visitedTrans);
                EventHandler<ActionEvent> handler = searchTransition.getOnFinished();
                searchTransition.setOnFinished(ev -> {
                    if (handler != null)
                        handler.handle(ev);
                    playBtn.fireEvent(new ActionEvent(playBtn, null));
                });
                searchTransition.play();
            }
        });
        begBtn.setOnAction(e-> {
            controller.setMode(2);
            controller.reload();
            tubeMap.update(controller);
            tubeMap.zoomLevel(3.2);
        });
        medBtn.setOnAction(e-> {
            controller.setMode(1);
            controller.reload();
            tubeMap.update(controller);
            tubeMap.zoomLevel(0.79);
        });
        hardBtn.setOnAction(e-> {
            controller.setMode(0);
            controller.reload();
            tubeMap.update(controller);
            tubeMap.zoomLevel(0.5);

        });

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

        StackPane keyPane = new StackPane();

        ArrayList <String> lineNames = new ArrayList<>();
        ArrayList <Color> lineColors = new ArrayList<>();
        ArrayList<Text> lineTitles = new ArrayList<>();

        lineNames.add("Bakerloo");
        lineColors.add(Color.BROWN);
        lineNames.add("Central");
        lineColors.add(Color.RED);
        lineNames.add("Circle");
        lineColors.add(Color.YELLOW);
        lineNames.add("District");
        lineColors.add(Color.GREEN);
        lineNames.add("Hammersmith & City");
        lineColors.add(Color.PINK);
        lineNames.add("Jubilee");
        lineColors.add(Color.GREY);
        lineNames.add("Northern");
        lineColors.add(Color.BLACK);
        lineNames.add("Overground");
        lineColors.add(Color.ORANGE);
        lineNames.add("Piccadilly");
        lineColors.add(Color.BLUE);
        lineNames.add("Waterloo & City");
        lineColors.add(Color.CORAL);

        VBox names = new VBox();
        names.setSpacing(2);
        names.setAlignment(Pos.CENTER);

        VBox colors = new VBox();
        colors.setSpacing(2);

        lineTitles.addAll(lineNames.stream().map(Text::new).collect(Collectors.toList()));

        for (Text t:lineTitles) {
            t.setFont(new Font(25));
            t.setFill(Color.WHITE);
            t.setStyle("-fx-stroke: black;-fx-stroke-width: 0.5;");
            names.getChildren().add(t);
        }

        for (Color c:lineColors) {
            colors.getChildren().add(new Rectangle(280,30,c));
        }

        keyPane.getChildren().addAll(colors,names);

        topBox.getChildren().addAll(spacer, playBtn,begBtn, medBtn, hardBtn, searcherCbx);
        bottomBox.getChildren().addAll(prevBtn, bottomLPad, bottomCentre, bottomRPad, nextBtn);
        sideBox.getChildren().addAll(keyPane);

        contentBox.setPrefWidth(PARTH.WIDTH);
        contentBox.setPrefHeight(PARTH.HEIGHT - topBox.getPrefHeight() - bottomBox.getPrefHeight());
    }

    @Override
    public void onOpen() {
        tubeMap = new TubeMap(controller, mapAspectRatio, contentBox.getPrefWidth(), contentBox.getPrefHeight(), skybox);
        tubeMap.initialise();
        tubeMap.zoomLevel(0.5);

        tubeMap.setClip(new Rectangle(contentBox.getPrefWidth(), contentBox.getPrefHeight()));
        contentBox.getChildren().add(tubeMap);

        searcherCbx.setValue(searcherCbx.getItems().get(0));
        loadTutorial(searcherCbx.getValue().getClazz().getName());
        controller.setSearcher(searcherCbx.getValue());
    }

    @Override
    public void loadResources(Map<String, Object> res) {
        // Load skybox image
        skybox = (Image) res.get("skybox");
        LOG.info("skybox loaded");
        // Load key image
        key = (Image) res.get("key");
        keyImage.setFill(new ImagePattern(key));
        LOG.info("key loaded");

        JsonObject metadata = ((JsonObject) res.get("stationinfo")).get("metadata").asObject();
        mapAspectRatio = metadata.getDouble("aspectratio", 0);
    }

    public void loadTutorial(String tutorialType) {
        tutorial = controller.getTutorial(tutorialType);
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

    public void updateText(String key, Object... args) {
        String desc = String.format(tutorial.getInstruction(key).getDesc(), args);
        tutorialTitle.setText(tutorial.getInstruction(key).getTitle());
        tutorialDesc.setText("\n" + desc);
    }

    public TubeMap getMap() {
        return tubeMap;
    }

    @Override
    public BaseController getController() {
        return controller;
    }

    public Parent getWindow() {
        return frontPane.getParent();
    }
}
