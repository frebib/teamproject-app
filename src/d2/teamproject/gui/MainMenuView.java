package d2.teamproject.gui;

import d2.teamproject.visualisations.BaseModule;
import javafx.geometry.Pos;
import javafx.scene.effect.BoxBlur;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.List;

public class MainMenuView {
    private StackPane menuPane;

    public MainMenuView(List<BaseModule> modules, VisView visView) {
        menuPane = new StackPane();
        VBox menu = new VBox(24);

        menu.setAlignment(Pos.CENTER);
        for (BaseModule module : modules)
            menu.getChildren().add(image(module.getBanner(), module.getName()));

        menuPane.getChildren().add(menu);
    }

    public StackPane getMenu() {
        return menuPane;
    }

    /**
     * @param banner This is the location of the image used for the button
     * @param text   This is the text that will appear on hovering over the image
     * @return a fully set-up StackPane for use in the vertical box
     */
    private StackPane image(Image banner, String text) {
        /* Declarations */
        Text t = new Text(10, 50, text);
        t.setFont(new Font(75));
        t.setFill(Color.WHITE);
        t.setVisible(false);
        t.setStyle("-fx-stroke: black;-fx-stroke-width: 1;");
        StackPane sp = new StackPane();
        ImageView imgView = new ImageView(banner); //Adds the Image to a new ImageView
        sp.getChildren().add(imgView);  // Stack Pane -> ImageView -> Image
        sp.getChildren().add(t);

        /* Effects */
        ColorAdjust darken = new ColorAdjust(0, 0, -0.5, 0);
        ColorAdjust none = new ColorAdjust(0, 0, 0, 0);
        BoxBlur blur = new BoxBlur(4, 4, 4);
        blur.setInput(darken);
        BoxBlur focus = new BoxBlur(0, 0, 0);
        focus.setInput(none);
        imgView.setEffect(focus);

        /* Events */
        t.setOnMouseEntered(e -> {
            imgView.setEffect(blur);
            t.setVisible(true);
        });
        t.setOnMouseExited(e -> {
            imgView.setEffect(focus);
            t.setVisible(false);
        });
        imgView.setOnMouseEntered(e -> {
            imgView.setEffect(blur);
            t.setVisible(true);
        });
        imgView.setOnMouseExited(e -> {
            imgView.setEffect(focus);
            t.setVisible(false);
        });
        imgView.setOnMouseClicked(e -> {
//            mMenu.setVisible(false);
//            pane.setVisible(true);
            System.out.println("Clicked " + t.getText());
        });
        return sp;
    }

    /*
    @Deprecated
    public StackPane BasicScene() {
        // Border pane implementation - Holds all the different sections
        BorderPane bp = new BorderPane();
        bp.setPadding(new Insets(10, 20, 10, 20));

        // Back button implementation - Used to go back to the main menu
        Button backButton = new Button("BACK");
        HBox topBox = new HBox();
        topBox.getChildren().addAll(backButton);
        backButton.setOnMouseClicked(e -> {
//            mMenu.setVisible(true);
//            pane.setVisible(false);
        });

        // Center pane implementation - Used to visualise the algorithms
        StackPane centerPane = new StackPane();
        // CALL SPECIFIC CLASS HERE

        // Help button implementation - Used to initialise the tutorial mode
        Button helpButton = new Button("HELP");
        HBox helpButtonBox = new HBox();
        helpButtonBox.getChildren().addAll(helpButton);
        helpButtonBox.alignmentProperty().setValue(Pos.CENTER_LEFT);

        // Tick button implementation - Use to check the users' work
        Button tickButton = new Button("TICK");
        HBox tickButtonBox = new HBox();
        tickButtonBox.getChildren().addAll(tickButton);
        tickButtonBox.alignmentProperty().setValue(Pos.CENTER_RIGHT);

        // Pane to hold the bottom gui elements
        StackPane bottomBox = new StackPane();
        bottomBox.getChildren().addAll(helpButtonBox, tickButtonBox);

        bp.setTop(topBox);
        bp.setCenter(centerPane);
        bp.setBottom(bottomBox);
        pane.getChildren().addAll(bp);
        return pane;
    } */
}