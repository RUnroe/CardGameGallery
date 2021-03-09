package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("../main/main-scene.fxml"));
        Parent sceneRoot = fxmlLoader.load();
        primaryStage.setTitle("Forest's Fallacy");
        primaryStage.setScene(new Scene(sceneRoot));
        primaryStage.show();
        MainScene mainScene = fxmlLoader.getController();
        mainScene.setStage(primaryStage);

    }

    //region Poker Start Artifact Code
    //        FXMLLoader fxmlLoader = new FXMLLoader();
    //        fxmlLoader.setLocation(getClass().getResource("../poker/views/poker-home-scene.fxml"));
    //        AnchorPane anchorPane = fxmlLoader.load();
    //        primaryStage.setTitle("Poker");
    //        primaryStage.setScene(new Scene(anchorPane));
    //endregion

    public static void main(String[] args) {
        launch(args);
    }
}
