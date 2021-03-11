package gofish.presenters;

import gofish.GoFish;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import models.Card;
import models.ERank;
import models.ESuit;
import models.Player;
import war.controllers.EngineOfWar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class GoFishGameScene {
    //Options/Menu screen
//    public Button ChooseFileBtn;
//    public Label LoadedFileName;
//    public Button LoadGameBtn;
//    public CheckBox PlayAgainstComputer;
//    public TextField Player1NameInput;
//    public TextField Player2NameInput;
//    public Button CreateGameBtn;

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

//    private static EngineOfWar engine;
    public Label lblCurrentPlayerName;
    public HBox hBoxCurrentPlayerHand;
    public Spinner<ERank> spinnerRank;
//    public Spinner<ESuit> spinnerSuit;
    public Spinner<String> spinnerPlayers;
    public VBox vBoxAskedPlayerPrompt;
    public HBox hBoxCurrentPlayerActions;
    public Label lblGiver;

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
//        engine = new GoFish();
    }

    public void setupSpinners() {
        SpinnerValueFactory<ERank> svfRank = new SpinnerValueFactory.ListSpinnerValueFactory<ERank>(FXCollections.observableArrayList(ERank.values()));
//        SpinnerValueFactory<ESuit> svfSuit = new SpinnerValueFactory.ListSpinnerValueFactory<ESuit>(FXCollections.observableArrayList(ESuit.values()));
        String[] playerNames = new String[GoFish.getPlayers().length];
        for (int playerIndex = 0; playerIndex < playerNames.length; playerIndex++) {
            playerNames[playerIndex] = GoFish.getPlayers()[playerIndex].getName();
        }
        SpinnerValueFactory<String> svfPlayerNames = new SpinnerValueFactory.ListSpinnerValueFactory<String>(FXCollections.observableArrayList(playerNames));
        spinnerRank.setValueFactory(svfRank);
//        spinnerSuit.setValueFactory(svfSuit);
        spinnerPlayers.setValueFactory(svfPlayerNames);
    }

    public void updateCurrentPlayerDisplay(Player player) {
        if (hBoxCurrentPlayerHand == null) {
            hBoxCurrentPlayerHand = new HBox();
        }
        lblCurrentPlayerName.setText(player.getName());
        hBoxCurrentPlayerHand.getChildren().clear();
        player.getHand().forEach(card -> {
            hBoxCurrentPlayerHand.getChildren().add(card.getImageView());
        });
        toggleCurrentPlayerDislay();
    }

//    public void toggleIncludingAI(ActionEvent actionEvent) {
//        if(PlayAgainstComputer.isSelected()) {
//            Player2NameInput.setText("Computer");
//            Player2NameInput.setEditable(false);
//        }
//        else {
//            Player2NameInput.setText("Player 2");
//            Player2NameInput.setEditable(true);
//        }
//    }

//    public void createGame(ActionEvent actionEvent) {
//        GoFish.setup(new Player(Player1NameInput.getText()), new Player(Player2NameInput.getText()));
//        changeScene((Stage) CreateGameBtn.getScene().getWindow(), "../views/go-fish-game-scene.fxml");
//
////        engine = new GoFish();
//    }

    public void quitGame(ActionEvent actionEvent) {
        changeScene((Stage) QuitGameBtn.getScene().getWindow(), "../views/war-home-scene.fxml");
    }

    private void gameWon(Player winningPlayer) {
        StartTurnBtn.setDisable(true);
        setText(winningPlayer.getName() + " won the game!");
    }

    private void updatePlayerNames() {
        Player1NameDisplay.setText(GoFish.getPlayers()[0].getName());
        Player2NameDisplay.setText(GoFish.getPlayers()[1].getName());
    }

    public void playRound(ActionEvent actionEvent) {
        //while(engine.checkForWin() == -1) {
//        updatePlayerNames();
        updateCurrentPlayerDisplay(GoFish.getPlayers()[GoFish.getPlayerTurn()]);
//        updateCardCountDisplay();
        //Make each player place their card on the board
//        for (int i = 0; i < GoFish.getPlayers().length; i++) {
//            engine.getNextCard();
//        }
//        determineWinnerOfRound();
//        //}
//        if(engine.checkForWin() > -1) gameWon(engine.getModel().getPlayers()[engine.checkForWin()]);
    }


//    private void determineWinnerOfRound() {
//        int winner = engine.determineRoundWinner();
//        if(winner == 0) {
//            setText(engine.getModel().getPlayers()[0].getName() + " won the round!");
//            updateBoardDisplay();
//        }
//        else if (winner == 1) {
//            setText(engine.getModel().getPlayers()[1].getName() + " won the round!");
//            updateBoardDisplay();
//        }
//        else {
//            setText("War!");
//            Card[][] warTable = engine.goToWar(new int[]{0, 1}, false);
//            handleWar(warTable, true);
//        }
//    }

//    private void handleWar(Card[][] warTable, boolean emptyCardDisplays) {
//        int warWinner = engine.checkWarWinner(warTable);
//        updateBoardDisplay(warTable, emptyCardDisplays);
//        if(warWinner == -1) { //Tie
//            setText("Double War!");
//            handleWar(engine.goToWar(new int[]{0, 1}, true), false);
//        }
//        else if(warWinner == 0) setText(engine.getModel().getPlayers()[0].getName() + " won the war!");
//        else if(warWinner == 1) setText(engine.getModel().getPlayers()[1].getName() + " won the war!");
//    }
    private void setText(String s) {
        OutputTxt.setText(s);
    }

//    private void updateCardCountDisplay() {
//        Player1CardCountDisplay.setText(engine.getModel().getPlayers()[0].getHand().size() + " cards");
//        Player2CardCountDisplay.setText(engine.getModel().getPlayers()[1].getHand().size() + " cards");
//    }

//    private void updateBoardDisplay() {
//        System.out.println(engine.getModel().getTable()[0].getRank() + " " + engine.getModel().getTable()[1].getRank());
//        //Empty out card displays
//        Player1CardDisplay.getChildren().clear();
//        Player2CardDisplay.getChildren().clear();
//        //Populate displays with current cards
//        Player1CardDisplay.getChildren().add(engine.getModel().getTable()[0].getImageView());
//        Player2CardDisplay.getChildren().add(engine.getModel().getTable()[1].getImageView());
//
//    }

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

    public void onActionAskForCard(ActionEvent actionEvent) {
        String giverName = spinnerPlayers.getValue();
        if (!giverName.equals(GoFish.getPlayers()[GoFish.getPlayerTurn()].getName())) {
            hBoxCurrentPlayerHand.setVisible(false);
            hBoxCurrentPlayerActions.setVisible(false);
            hBoxCurrentPlayerActions.setDisable(true);
            vBoxAskedPlayerPrompt.setVisible(true);
            vBoxAskedPlayerPrompt.setDisable(false);
            lblGiver.setText(giverName);
            //TODO display prompt for asked player to respond.
        } else {
            // TODO display message saying you can't ask yourself
        }
    }

    public void onActionGiveCards(ActionEvent actionEvent) {
        ERank askedForCardRank = spinnerRank.getValue();
        Player giver = null;
        for (Player player : GoFish.getPlayers()) {
            if (player.getName().equals(spinnerPlayers.getValue())) {
                giver = player;
                break;
            }
        }
        if (giver != null) {
            List<Card> matches = new ArrayList<>();
            for(Card card : giver.getHand()) {
                if (card.getRank().equals(askedForCardRank)) {
                    matches.add(card);
                }
            }
            if (matches.size() > 0) {
                GoFish.getPlayers()[GoFish.getPlayerTurn()].getHand().addAll(matches);
                giver.getHand().removeAll(matches);
                GoFish.checkHandForBook(GoFish.getPlayerTurn());
            } else {
                onActionGoFish(null);
            }
        }
    }

    public void onActionGoFish(ActionEvent actionEvent) {
        GoFish.nextTurn();
        updateCurrentPlayerDisplay(GoFish.getPlayers()[GoFish.getPlayerTurn()]);
    }

    public void toggleCurrentPlayerDislay() {
        hBoxCurrentPlayerHand.setVisible(!hBoxCurrentPlayerHand.isVisible());
        hBoxCurrentPlayerActions.setVisible(!hBoxCurrentPlayerActions.isVisible());
        hBoxCurrentPlayerActions.setDisable(!hBoxCurrentPlayerActions.isDisable());
        vBoxAskedPlayerPrompt.setVisible(!vBoxAskedPlayerPrompt.isVisible());
        vBoxAskedPlayerPrompt.setDisable(!vBoxAskedPlayerPrompt.isDisable());
    }

}
