package gofish.presenters;

import gofish.GoFish;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import models.Card;
import models.CardComparator;
import models.ERank;
import models.Player;

import java.io.IOException;
import java.util.*;

public class GoFishGameScene {
    //Options/Menu screen
//    public Button ChooseFileBtn;
//    public Label LoadedFileName;
//    public Button LoadGameBtn;
//    public CheckBox PlayAgainstComputer;
//    public TextField Player1NameInput;
//    public TextField Player2NameInput;
//    public Button CreateGameBtn;

    //region Game screen
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
    public VBox vBoxPlayerActions;
    public Label lblGiver;
    public VBox vBoxPlayerHand;
    public Button btnTogglePlayerHand;
    public VBox vBoxScoreboard;
    public Label lblGoFish;
    public VBox vBoxBooks;
    //endregion

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

    public void updateSpinners() {
        List<ERank> heldRanks = new ArrayList<>();
//        SpinnerValueFactory<ESuit> svfSuit = new SpinnerValueFactory.ListSpinnerValueFactory<ESuit>(FXCollections.observableArrayList(ESuit.values()));
        List<String> playerNames = new ArrayList<>();
        for (Player player : GoFish.getPlayers()) {
            if (!player.getName().equals(GoFish.getPlayers()[GoFish.getPlayerTurn()].getName())) {
                playerNames.add(player.getName());
            } else {
                for (Card card : player.getHand()) {
                    if (!heldRanks.contains(card.getRank())) {
                        heldRanks.add(card.getRank());
                    }
                }
            }
        }
        SpinnerValueFactory<ERank> svfRank = new SpinnerValueFactory.ListSpinnerValueFactory<ERank>(FXCollections.observableArrayList(heldRanks));
        SpinnerValueFactory<String> svfPlayerNames = new SpinnerValueFactory.ListSpinnerValueFactory<String>(FXCollections.observableArrayList(playerNames));
        spinnerRank.setValueFactory(svfRank);
        spinnerPlayers.setValueFactory(svfPlayerNames);
    }

    public void updateDisplay(Player player) {
        if (hBoxCurrentPlayerHand == null) {
            hBoxCurrentPlayerHand = new HBox();
        }
        lblCurrentPlayerName.setText(player.getName() + " | " + player.getScore() + "pts");
        hBoxCurrentPlayerHand.getChildren().clear();
        player.getHand().forEach(card -> {
            hBoxCurrentPlayerHand.getChildren().add(card.getImageView());
        });
//        toggleCurrentPlayerDislay();
        updateBookDisplay();
    }

    private void updateBookDisplay() {
        vBoxBooks.getChildren().clear();
        for (ERank book : GoFish.getCompletedBooks()) {
            vBoxBooks.getChildren().add(new Label(book.name()));
        }
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
        changeScene((Stage) QuitGameBtn.getScene().getWindow(), "../views/go-fish-home-scene.fxml");
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
        hBoxCurrentPlayerHand.getChildren().clear();
        for (Player player : GoFish.getPlayers()) {
            player.getHand().clear();
        }
        GoFish.setup(GoFish.getPlayers());
        updateDisplay(GoFish.getPlayers()[GoFish.getPlayerTurn()]);
        vBoxPlayerHand.setVisible(true);
        vBoxPlayerActions.setVisible(true);

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
        Player giver = null;
        for (Player player : GoFish.getPlayers())
            if (giverName.equals(player.getName())) {
                giver = player;
                break;
            }
        assert giver != null;
        if (!giver.isPlayerAI()) {
//            hBoxCurrentPlayerHand.setVisible(false);
//            hBoxCurrentPlayerActions.setVisible(false);
//            hBoxCurrentPlayerActions.setDisable(true);
//            vBoxAskedPlayerPrompt.setVisible(true);
//            vBoxAskedPlayerPrompt.setDisable(false);
            toggleCurrentPlayerDisplay();
            lblGiver.setText(giverName);
            onActionTogglePlayerHud(null);
            giver.getHand().sort(new CardComparator());
            updateDisplay(giver);
            //TODO display prompt for asked player to respond.
        } else {
            onActionGiveCards(null);
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
                    System.out.println(card.getName());
                    matches.add(card);
                }
            }
            if (matches.size() > 0) {
                GoFish.getPlayers()[GoFish.getPlayerTurn()].getHand().addAll(matches);
                GoFish.getPlayers()[GoFish.getPlayerTurn()].getHand().sort(new CardComparator());
                giver.getHand().removeAll(matches);
                GoFish.checkHandForBook(GoFish.getPlayerTurn());
                giver.getHand().sort(new CardComparator());
                if (!giver.isPlayerAI()) {
                    toggleCurrentPlayerDisplay();
                    onActionTogglePlayerHud(null);
                }
                GoFish.checkHandForBook(GoFish.getPlayerTurn());
                updateDisplay(GoFish.getPlayers()[GoFish.getPlayerTurn()]);
                updateSpinners();
                if (GoFish.getPlayers()[GoFish.getPlayerTurn()].isPlayerAI()) {
                    GoFish.aiTurn();
                }
            } else {
                onActionGoFish(null);
            }
        }
    }

    public void onActionGoFish(ActionEvent actionEvent) {
        if (GoFish.getDeck().getCards().size() > 0) {

            GoFish.getPlayers()[GoFish.getPlayerTurn()].drawFromDeck(GoFish.getDeck());
            GoFish.getPlayers()[GoFish.getPlayerTurn()].getHand().sort(new CardComparator());
            GoFish.nextTurn();
            GoFish.getPlayers()[GoFish.getPlayerTurn()].getHand().sort(new CardComparator());
            GoFish.checkHandForBook(GoFish.getPlayerTurn());
            updateDisplay(GoFish.getPlayers()[GoFish.getPlayerTurn()]);
            updateSpinners();
//        for (int i = 0; i < GoFish.getPlayers().length; i++) {
//            GoFish.getPlayers()[i].setScore(18 + i*2);
//        }
            int booksCompleted = 0;
            for (Player player : GoFish.getPlayers()) {
                booksCompleted += player.getScore();
            }
            booksCompleted /= 2;
            if (GoFish.getDeck().getCards().size() < 1 || booksCompleted >= 13) {
                displayScoreBoard();
            }
            if (GoFish.getPlayers()[GoFish.getPlayerTurn()].isPlayerAI()) {
                boolean isCardFound;
                do {
                    isCardFound = GoFish.aiTurn();
                } while (isCardFound);
                onActionGoFish(null);
            } else {
                toggleCurrentPlayerDisplay();
            }
            vBoxAskedPlayerPrompt.setVisible(false);
            vBoxPlayerHand.setVisible(false);
            vBoxPlayerActions.setVisible(false);
            lblGoFish.setVisible(true);
        } else {
            displayScoreBoard();
        }
    }

    private void displayScoreBoard() {
        vBoxPlayerHand.setVisible(false);
        vBoxPlayerActions.setVisible(false);
        vBoxAskedPlayerPrompt.setVisible(false);
        btnTogglePlayerHand.setVisible(false);
        lblGoFish.setVisible(false);
        List<Player> rankedPlayers = Arrays.asList(GoFish.getPlayers());
        rankedPlayers.sort(new Comparator<Player>() {
            @Override
            public int compare(Player playerA, Player playerB) {
                return playerB.getScore() - playerA.getScore();
            }
        });
        for (Player player : rankedPlayers) {
            vBoxScoreboard.getChildren().add(new Label(player.getName() + ": " + player.getScore()));
        }
    }

    public void toggleCurrentPlayerDisplay() {
        vBoxAskedPlayerPrompt.setVisible(!vBoxAskedPlayerPrompt.isVisible());
//        vBoxPlayerActions.setDisable(!vBoxPlayerActions.isDisable());
//        vBoxAskedPlayerPrompt.setDisable(!vBoxAskedPlayerPrompt.isDisable());
    }

    public void onActionTogglePlayerHud(ActionEvent actionEvent) {
        vBoxPlayerHand.setVisible(!vBoxPlayerHand.isVisible());
        vBoxPlayerActions.setVisible(!vBoxAskedPlayerPrompt.isVisible());
        lblGoFish.setVisible(false);
    }
}
