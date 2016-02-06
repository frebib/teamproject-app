package d2.teamproject.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
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


public class MainMenuTest extends Application {

    String blocksImage = "d2/teamproject/gui/images/blocks.jpg";
    String solarImage = "d2/teamproject/gui/images/planets.jpg";
    String tubeImage = "d2/teamproject/gui/images/tube.png";

    @Override
    public void start(Stage primaryStage) throws Exception{

        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("PARTH");
        primaryStage.setMaximized(true);
        double X = primaryStage.getWidth();
        double Y = primaryStage.getHeight();

        StackPane tube = image(tubeImage,"London Underground",X,Y);
        StackPane solar = image(solarImage,"Planets",X,Y);
        StackPane blocks = image(blocksImage,"Building Blocks",X,Y);

        VBox vbox = new VBox(25);
        vbox.setAlignment(Pos.CENTER);
        vbox.getChildren().addAll(tube,solar,blocks);

        Scene scene = new Scene(vbox);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public StackPane image(String loc, String text, double width, double height){
        //Declarations
        Text t = new Text(10, 50, text);
        t.setFont(new Font(75));
        t.setFill(Color.WHITE);
        t.setVisible(false);

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
        BoxBlur unblur = new BoxBlur(0,0,0);
        unblur.setInput(none);

        //System.out.println(viewpoint.getHeight());
        //double newWidth = viewpoint.getWidth();
        //double newHeight = viewpoint.getHeight();

        //Events
        imgView.setEffect(unblur);

        t.setOnMouseEntered(e -> {
            imgView.setEffect(blur);
            t.setVisible(true);
        });

        t.setOnMouseExited(e -> {
            imgView.setEffect(unblur);
            t.setVisible(false);
        });

        imgView.setOnMouseEntered(e -> {
            imgView.setEffect(blur);
            t.setVisible(true);
        });

        imgView.setOnMouseExited(e -> {
            imgView.setEffect(unblur);
            t.setVisible(false);
        });
        imgView.setOnMouseClicked(e -> System.out.println("Clicked"));
        return sp;
    }
    public static void main(String[] args) {
        launch(args);
    }
}
