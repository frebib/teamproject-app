import javafx.application.Application;
import javafx.fxml.FXMLLoader;
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


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("PARTH");

        String url1 = "http://mikecann.co.uk/wp-content/uploads/2009/12/javafx_logo_color_1.jpg";
        String blocks = "http://i.imgur.com/XfHVULM.jpg";
        String solar = "http://i.imgur.com/UPLMqlO.jpg";
        String tube = "http://i.imgur.com/eM2qLSM.jpg";
        String tubeL = "http://i.imgur.com/eM2qLSM.jpg";
        String text = "This is a test";

        //image(blocks),image(solar),
        VBox vbox = new VBox();
        vbox.getChildren().addAll(image(tube,"One"),image(tube,"Two"),image(tube,"Three"));
        Scene scene = new Scene(vbox);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public StackPane image(String loc, String text){
        //Declarations

        Text t = new Text(10, 50, text);
        t.setFont(new Font(60));
        t.setFill(Color.WHITE);
        t.setVisible(false);

        StackPane sp = new StackPane();
        Image img = new Image(loc,545,270,false,false); //Uses a url to as the Image
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
        //Events
        imgView.setEffect(unblur);

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
