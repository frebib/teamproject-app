package d2.teamproject.gui;

import javafx.application.Application;
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

public class MainMenuView extends Application {
    public VBox mMenu = new VBox(25);
    public Stage secondaryStage = new Stage();
    public BasicView basicView = new BasicView();
    public Scene mMenuScene;

    /**
     * @param primaryStage The Primary Stage
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception{
        secondaryStage.setTitle("PARTH");
        secondaryStage.setMaximized(true);

        String IMAGE_BLOCKS = "d2/teamproject/gui/images/blocks.jpg";
        String IMAGE_SOLAR = "d2/teamproject/gui/images/planets.jpg";
        String IMAGE_TUBE = "d2/teamproject/gui/images/tube.png";
        StackPane tube = image(IMAGE_TUBE,"Searching Algorithms");
        StackPane solar = image(IMAGE_SOLAR,"Sorting Algorithms");
        StackPane blocks = image(IMAGE_BLOCKS,"Stack & Queue");

        mMenu.setAlignment(Pos.CENTER);
        mMenu.getChildren().addAll(tube,solar,blocks);

        mMenuScene = new Scene(mMenu);
        secondaryStage.setScene(mMenuScene);

        primaryStage = secondaryStage;
        primaryStage.show();
    }

    /**
     * @param loc This is the location of the image used for the button
     * @param text This is the text that will appear on hovering over the image
     * @return a fully set-up StackPane for use in the vertical box
     */
    private StackPane image(String loc, String text){
        //Declarations
        Text t = new Text(10, 50, text);
        t.setFont(new Font(75));
        t.setFill(Color.WHITE);
        t.setVisible(false);
        t.setStyle("-fx-stroke: black;-fx-stroke-width: 1;");
        StackPane sp = new StackPane();
        Image img = new Image(loc,1200,250,false,false); //Uses a url to as the Image
        ImageView imgView = new ImageView(img); //Adds the Image to a new ImageView
        sp.getChildren().add(imgView);  // Stack Pane -> ImageView -> Image
        sp.getChildren().add(t);
        //Effects
        ColorAdjust darken= new ColorAdjust(0,0,-0.5,0);
        ColorAdjust none = new ColorAdjust(0,0,0,0);
        BoxBlur blur = new BoxBlur(4,4,4);
        blur.setInput(darken);
        BoxBlur focus = new BoxBlur(0,0,0);
        focus.setInput(none);
        imgView.setEffect(focus);
        //Events
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
            mMenu.setVisible(false);
            secondaryStage.setScene(basicView.BasicScene());
            System.out.println("Clicked "+t.getText());
        });
        return sp;
    }


    public static void main(String[] args) {
        launch(args);
    }
}
