package main;

import blackjack.controllers.BlackjackEngine;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class MainScene {
    //region GUI Objects
    public Button btnPlayPoker;
    public Button btnPlayBlackjack;
    public Button btnPlayGoFish;
    public Button btnPlayWar;

    //endregion

    //region Stage Handling
    private Stage stage;

//    public void initialize() {
//        getStage().hide();
//    }

    public Stage getStage() {
        if (stage == null) {
            stage = new Stage();
        }
        return stage;
    }


    public void setStage(Stage stage) {
        this.stage = stage;
    }
    //endregion


    //region Button Click Event Handlers
    public void onActionBtnPlayBlackjack(ActionEvent actionEvent) {
        new BlackjackEngine().setup(getStage());
    }

    public void onActionBtnPlayPoker(ActionEvent actionEvent) {
        loadScene("../poker/views/poker-home-scene.fxml");
    }

    public void onActionBtnPlayGoFish(ActionEvent actionEvent) {
    }

    public void onActionBtnPlayWar(ActionEvent actionEvent) {
        loadScene("../war/views/war-home-scene.fxml");
    }

    //endregion

    public void loadScene(String path) {
        try {
            stage = new Stage();
            Parent sceneRoot = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(path)));
            //getStage().initOwner(btnPlayPoker.getScene().getWindow());
            getStage().setScene(new Scene(sceneRoot));

            //getStage().showAndWait();
            getStage().show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
