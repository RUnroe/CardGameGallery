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
import models.Card;
import models.Player;
import war.controllers.EngineOfWar;
import war.models.WarModel;

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

    private static EngineOfWar engine;

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
        //Change this later. Pass in deserialized file
        engine = new EngineOfWar(new WarModel());
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
        engine = new EngineOfWar();
        engine.start(new Player(Player1NameInput.getText()), new Player(Player2NameInput.getText()));
    }

    public void quitGame(ActionEvent actionEvent) {
        changeScene((Stage) QuitGameBtn.getScene().getWindow(), "../views/war-home-scene.fxml");
    }

    public void playRound(ActionEvent actionEvent) {
        updateCardCountDisplay();
        //Make each player place their card on the board
        for(int i = 0; i < engine.getModel().getPlayers().length; i++) {
            engine.getNextCard();
        }
        determineWinner();

    }
    private void determineWinner() {
        int winner = engine.determineRoundWinner();
        if(winner == 0) {
            setText(engine.getModel().getPlayers()[0].getName() + " won the round!");
            updateBoardDisplay();
        }
        else if (winner == 1) {
            setText(engine.getModel().getPlayers()[1].getName() + " won the round!");
            updateBoardDisplay();
        }
        else {
            setText("War!");
            Card[][] warTable = engine.goToWar(new int[]{0, 1}, false);
            handleWar(warTable, true);
        }
    }

    private void handleWar(Card[][] warTable, boolean emptyCardDisplays) {
        int warWinner = engine.checkWarWinner(warTable);
        updateBoardDisplay(warTable, emptyCardDisplays);
        if(warWinner == -1) { //Tie
            setText("Double War!");
            handleWar(engine.goToWar(new int[]{0, 1}, true), false);
        }
        else if(warWinner == 0) setText(engine.getModel().getPlayers()[0].getName() + " won the war!");
        else if(warWinner == 1) setText(engine.getModel().getPlayers()[1].getName() + " won the war!");
    }
    private void setText(String s) {
        OutputTxt.setText(s);
    }

    private void updateCardCountDisplay() {
        Player1CardCountDisplay.setText(engine.getModel().getPlayers()[0].getHand().size() + " cards");
        Player2CardCountDisplay.setText(engine.getModel().getPlayers()[1].getHand().size() + " cards");
    }

    private void updateBoardDisplay() {
        System.out.println(engine.getModel().getTable()[0].getRank() + " " + engine.getModel().getTable()[1].getRank());
        //Empty out card displays
        Player1CardDisplay.getChildren().clear();
        Player2CardDisplay.getChildren().clear();
        //Populate displays with current cards
        Player1CardDisplay.getChildren().add(engine.getModel().getTable()[0].getImageView());
        Player2CardDisplay.getChildren().add(engine.getModel().getTable()[1].getImageView());

    }

    private void updateBoardDisplay(Card[][] warTable, boolean emptyCardDisplays) {
        System.out.println(warTable[0][0].getRank() + " ===== " + warTable[1][0].getRank());
        System.out.println(warTable[0][warTable[0].length-1].getRank() + " " + warTable[1][warTable[1].length-1].getRank());
        HBox[] displays = new HBox[]{Player1CardDisplay, Player2CardDisplay};
        //loop through each card in list per player
        for(int i = 0; i < warTable.length; i++) {
            //Empty out card displays
            if(emptyCardDisplays) displays[i].getChildren().clear();

            if(i % 2 == 0) { //Left side of screen
                //Reverse display order
                System.out.println("reverse right side");
                for (int j = warTable[i].length-1; j >= 0; j--) {
                    displays[i].getChildren().add(warTable[i][j].getImageView());
                }
            }
            else { //Right side of screen
                for (int j = 0; j < warTable[i].length; j++) {
                    displays[i].getChildren().add(warTable[i][j].getImageView());
                }
            }
        }

    }
}
