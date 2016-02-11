package d2.teamproject.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class BasicView extends Application{

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("PARTH");
        primaryStage.setMaximized(true);

        StackPane pane = new StackPane();
        BorderPane bp = new BorderPane();
        bp.setPadding(new Insets(10, 20, 10, 20));

        Button top = new Button("BACK");
        HBox topBox = new HBox();
        topBox.getChildren().addAll(top);

        StackPane centerPane = new StackPane();
        centerPane.setStyle("-fx-background-color: green");

        Button bottomLeft = new Button("HELP");
        Button bottomRight = new Button("TICK");
        //HBox bottomBox = new HBox();
        HBox bottomLeftBox = new HBox();
        HBox bottomRightBox = new HBox();
        StackPane bottomBox = new StackPane();

        bottomLeftBox.getChildren().addAll(bottomLeft);
        bottomLeftBox.alignmentProperty().setValue(Pos.CENTER_LEFT);
        bottomRightBox.getChildren().addAll(bottomRight);
        bottomRightBox.alignmentProperty().setValue(Pos.CENTER_RIGHT);
        bottomBox.getChildren().addAll(bottomLeftBox,bottomRightBox);

        bp.setTop(topBox);
        bp.setCenter(centerPane);
        bp.setBottom(bottomBox);

        pane.getChildren().addAll(bp);

        Scene scene = new Scene(pane);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
