package d2.teamproject.module.tubesearch;

import d2.teamproject.gui.VisualisationView;
import d2.teamproject.module.BaseController;
import d2.teamproject.tutorial.Tutorial;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.SubScene;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.util.Map;

import static d2.teamproject.PARTH.LOG;

/**
 * @author Parth Chandratreya
 */
public class TubeSearchView extends VisualisationView {
    private final TubeSearchController controller;
    private final boolean tutorialMode;
    private final Text tutorialDesc;
    private final Text tutorialTitle;
    private final TextFlow tutorialText;
    private final String tutorialType = "search"; // Take this in when switching sorts
    private Tutorial tutorial;
    private Image skybox;

    /**
     * @param controller
     */
    public TubeSearchView(TubeSearchController controller) {
        this.controller = controller;
        tutorialText = new TextFlow();
        tutorialTitle = new Text();
        tutorialDesc = new Text();
        tutorialMode = true;

//        bottomBox.setStyle("-fx-background-color: #64BEF6");

        topBox.setPrefHeight(1000 * 0.05);
        bottomBox.setPrefHeight(1000 * 0.1);
    }

    @Override
    public BaseController getController() {
        return controller;
    }

    @Override
    public void loadResources(Map<String, Object> res) {
        // Load skybox image
        skybox = (Image) res.get("skybox");
        LOG.info("skybox loaded");
        // Load tutorial
        tutorial = controller.getTutorial(tutorialType);
        // Set spacing and alignment
        bottomBox.setSpacing(200.0);
        bottomBox.setAlignment(Pos.CENTER);
        // Set the font size
        tutorialTitle.setFont(new Font(25));
        tutorialDesc.setFont(new Font(15));
        // Set the text to initial step
        updateText("step");
        // Set test wrapping width
        tutorialText.setMaxWidth(500);

        tutorialText.getChildren().addAll(tutorialTitle, tutorialDesc);
        tutorialText.setVisible(tutorialMode);
        bottomBox.getChildren().addAll(tutorialText);
    }

    @Override
    public void onOpen() {
        TubeMap tubeMap = new TubeMap(controller, (int) (1000 * 1.1), (int) (750 * 1.1), skybox);
        tubeMap.initialise();
        double width = contentBox.getWidth();
        double height = contentBox.getHeight();

        contentBox.getChildren().add(tubeMap.getSubScene());

        contentBox.setMinHeight(height);
        contentBox.setMinWidth(width);

        contentBox.setPrefHeight(height);
        contentBox.setPrefWidth(width);

        contentBox.setMaxHeight(height);
        contentBox.setMaxWidth(width);
    }

    /**
     * @param key
     */
    private void updateText(String key) {
        tutorialTitle.setText(tutorial.getInstruction(key).getTitle());
        tutorialDesc.setText("\n" + tutorial.getInstruction(key).getDesc());
    }

    public Parent getWindow() {
        return frontPane.getParent();
    }
}
