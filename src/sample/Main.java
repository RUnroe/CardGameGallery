package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import models.Card;
import models.Suit;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("../poker/views/poker-home-scene.fxml"));
        AnchorPane anchorPane = fxmlLoader.load();
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(anchorPane));
        primaryStage.show();

        System.out.println(new Card(Suit.DIAMONDS, 1).getName());
    }


    public static void main(String[] args) {
        launch(args);
    }
}
