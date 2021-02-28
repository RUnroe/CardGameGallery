package war.presenters;

import blackjack.controllers.BlackjackEngine;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import war.controllers.EngineOfWar;

import java.io.IOException;
import java.util.Objects;

public class WarScene {
    //Options/Menu screen
    public Button ChooseFileBtn;
    public Label LoadedFileName;
    public Button LoadGameBtn;
    public CheckBox PlayAgainstComputer;
    public TextField Player1NameInput;
    public TextField Player2NameInput;
    public Button CreateGameBtn;

    //Game screen
    public Button StartTurnBtn;
    public Label Player1NameDisplay;
    public Label Player1CardCountDisplay;
    public Label Player2NameDisplay;
    public Label Player2CardCountDisplay;
    public HBox Player1CardDisplay;
    public HBox Player2CardDisplay;
    public Label OutputTxt;
    public Button QuitGameBtn;


    public void changeScene(Stage stage, String fxmlPath) {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(fxmlPath)));
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void findFile(ActionEvent actionEvent) {
    }

    public void loadGame(ActionEvent actionEvent) {
    }

    public void toggleIncludingAI(ActionEvent actionEvent) {
        if(PlayAgainstComputer.isSelected()) {
            Player2NameInput.setText("Computer");
            Player2NameInput.setEditable(false);
        }
        else {
            Player2NameInput.setText("Player 2");
            Player2NameInput.setEditable(true);
        }
    }

    public void createGame(ActionEvent actionEvent) {
        changeScene((Stage) CreateGameBtn.getScene().getWindow(), "../views/war-game-scene.fxml");
    }

    public void quitGame(ActionEvent actionEvent) {
        changeScene((Stage) QuitGameBtn.getScene().getWindow(), "../views/war-home-scene.fxml");
    }
}
