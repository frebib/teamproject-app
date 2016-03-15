package d2.teamproject.gui;

/**
 * @author Parth Chandratreya
 */

import d2.teamproject.PARTH;
import d2.teamproject.module.BaseController;
import d2.teamproject.module.JsonController;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.effect.BoxBlur;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.List;

public class MainMenuView {
    private final Stage stage;
    private Scene menu;
    private StackPane menuPane;

    public MainMenuView(List<BaseController> modules, Stage stage) {
        this.stage = stage;
        menuPane = new StackPane();
        menu = new Scene(menuPane, PARTH.WIDTH, PARTH.HEIGHT);

        VBox menuVbox = new VBox(24);
        menuVbox.setAlignment(Pos.CENTER);
        for (BaseController module : modules)
            menuVbox.getChildren().add(makeButton(module, module.getBanner(), module.getName()));

        menuPane.getChildren().add(menuVbox);
    }

    public Scene getScene() {
        return menu;
    }

    /**
     * @param banner This is the location of the image used for the button
     * @param text   This is the text that will appear on hovering over the image
     * @return a fully set-up StackPane for use in the vertical box
     */
    private StackPane makeButton(BaseController controller, Image banner, String text) {
        /* Declarations */
        Text t = new Text(10, 50, text);
        t.setFont(new Font(110));
        t.setFill(Color.WHITE);
        t.setVisible(false);
        t.setStyle("-fx-stroke: black;-fx-stroke-width: 2;");

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
        sp.setOnMouseEntered(e -> {
            imgView.setEffect(blur);
            t.setVisible(true);
        });
        sp.setOnMouseExited(e -> {
            imgView.setEffect(focus);
            t.setVisible(false);
        });
        sp.setOnMousePressed(e -> {
            controller.onOpen();
            Scene scene = controller.getView().getScene();
            // Use default stylesheets if none are specified
            if (controller instanceof JsonController && !((JsonController) controller).hasStyles()) {
                scene.getStylesheets().clear();
//                scene.getStylesheets().addAll(PARTH.getInstance().getStyles());
            }
            stage.setScene(scene);
        });
        return sp;
    }

    public void setVisible() {
        stage.setScene(menu);
    }
}