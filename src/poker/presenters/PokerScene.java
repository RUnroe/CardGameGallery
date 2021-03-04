package poker.presenters;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import models.Card;
import models.Player;
import poker.controllers.PokerEngine;
import poker.models.GameStage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class PokerScene {
    //Home scene
    public Button CreateGameBtn;
    public TextField Player4NameInput;
    public TextField Player3NameInput;
    public TextField Player2NameInput;
    public TextField Player1NameInput;
    public CheckBox PlayWithHouseCheckBox;
    public Spinner NumberOfPlayersInput;
    public Button LoadGameBtn;
    public Label LoadedFileName;
    public Button ChooseFileBtn;

    //Game scene
    public HBox CurrPlayerHandContainer;
    public Button StartTurnBtn;
    public Label CurrPlayerNameDisplay;
    public Label CurrPlayerBalanceDisplay;
    public Button QuitGameBtn;
    public Label LeftPlayerNameDisplay;
    public Label LeftPlayerBalanceDisplay;
    public Label MidPlayerNameDisplay;
    public Label MidPlayerBalanceDisplay;
    public Label RightPlayerNameDisplay;
    public Label RightPlayerBalanceDisplay;
    public Label BetMoneyDisplay;
    public Label PoolMoneyDisplay;
    public Label OutputTxt;
    public Button DiscardBtn;
    public HBox BetButtonContainer;
    public Button BetControlBtn;
    public Button RaiseControlBtn;
    public Button AllInControlBtn;
    public Button FoldControlBtn;
    public Button CallControlBtn;

    private int numberOfPlayers = 2;
    private static PokerEngine engine;

    //Runs on startup. Adds event listener to spinner
    public void initialize() {
        //Set event listener on spinner
        try {
            NumberOfPlayersInput.getEditor().textProperty().addListener((obs, oldValue, newValue) ->
                    changeNumberOfPlayersInput(Integer.parseInt(newValue)));
        } catch(Exception e) { }
    }

    public void changeScene(Stage stage, String fxmlPath) {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(fxmlPath)));
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Go back to home/menu screen
    public void quitGame(ActionEvent actionEvent) {
        changeScene((Stage) QuitGameBtn.getScene().getWindow(), "../views/poker-home-scene.fxml");
    }
    //Go to game page at start of game
    public void createGame(ActionEvent actionEvent) {
        //Create and instantiate player array list
        ArrayList<Player> playerList = getPlayerList();
        //create engine
        engine = new PokerEngine(playerList);
        //change scene to game scene
        changeScene((Stage) CreateGameBtn.getScene().getWindow(), "../views/poker-game-scene.fxml");
    }
    //Go to game page with game information
    public void loadGame(ActionEvent actionEvent) {

    }



    //Open choose file and let the user select a file
    public void findFile(ActionEvent actionEvent) {

    }


    private ArrayList<Player> getPlayerList() {
        ArrayList<Player> playerList = new ArrayList<>();
        //Create new players and add them into list
        TextField[] playerNameInputs = new TextField[]{Player1NameInput, Player2NameInput, Player3NameInput, Player4NameInput};
        for(int i = 0; i < numberOfPlayers; i++) {
            playerList.add(new Player(playerNameInputs[i].getText()));
        }
        //Make last player AI if user checked AI check box
        int numOfPlayers = playerList.size();
        if(PlayWithHouseCheckBox.isSelected()) playerList.get(numOfPlayers-1).setPlayerAI(true);
        return playerList;
    }

    //Callback when changing the spinner for number of players
    public void changeNumberOfPlayersInput(int numberOfPlayers) {
        this.numberOfPlayers = numberOfPlayers;
        switch(numberOfPlayers) {
            case 2:
                Player3NameInput.setDisable(true);
                Player4NameInput.setDisable(true);
                break;
            case 3:
                Player3NameInput.setDisable(false);
                Player4NameInput.setDisable(true);
                break;
            case 4:
                Player3NameInput.setDisable(false);
                Player4NameInput.setDisable(false);
                break;
        }
        adjustListForHouse();
    }

    //Toggle whether the house is playing or not
    public void toggleIncludingAI(ActionEvent actionEvent) {

        adjustListForHouse();
    }

    //Set the last available input field text to House if the user chose to play against the house.
    private void adjustListForHouse() {
        resetNameInputValues();
        if(PlayWithHouseCheckBox.isSelected()) {
            TextField[] textFields = new TextField[]{Player1NameInput, Player2NameInput, Player3NameInput, Player4NameInput};
            textFields[numberOfPlayers-1].setEditable(false);
            textFields[numberOfPlayers-1].setText("House");
        }
    }

    //Make all text input fields editable and reset the text values
    private void resetNameInputValues() {
        TextField[] textFields = new TextField[]{Player1NameInput, Player2NameInput, Player3NameInput, Player4NameInput};
        for(int i = 0; i < textFields.length; i++) {
            textFields[i].setEditable(true);
            textFields[i].setText("Player " + (i+1));
        }
    }


    private void setText(String s) {
        OutputTxt.setText(s);
    }
    private void updatePlayerDisplays() {
        //set current player in bottom left corner
        displayPlayer(engine.getModel().getCurrentPlayer(), 0);
        switch(engine.getModel().getPlayerList().size()) {
            case 2:
                displayPlayer(engine.getModel().getNextPlayer(1), 2);
                break;
            case 3:
                displayPlayer(engine.getModel().getNextPlayer(1), 1);
                displayPlayer(engine.getModel().getNextPlayer(2), 3);
                break;
            case 4:
                displayPlayer(engine.getModel().getNextPlayer(1), 1);
                displayPlayer(engine.getModel().getNextPlayer(2), 2);
                displayPlayer(engine.getModel().getNextPlayer(3), 3);
                break;
        }

    }

    private void displayPlayer(Player player, int positionIndex) {
        Label[] nameDisplayArray = new Label[]{CurrPlayerNameDisplay, LeftPlayerNameDisplay, MidPlayerNameDisplay, RightPlayerNameDisplay};
        Label[] balanceDisplayArray = new Label[]{CurrPlayerBalanceDisplay, LeftPlayerBalanceDisplay, MidPlayerBalanceDisplay, RightPlayerBalanceDisplay};
        //Make name and balance output visible
        nameDisplayArray[positionIndex].setVisible(true);
        balanceDisplayArray[positionIndex].setVisible(true);


        nameDisplayArray[positionIndex].setText(player.getName());
        balanceDisplayArray[positionIndex].setText("$" + player.getBank());
    }


    public void setBetRaiseValue(ActionEvent actionEvent) {
        //Make sure the buttons only do something when in the bet stage
        if(engine.getModel().getGameStage() == GameStage.BET) {
            //Get value out of button
            int selectedAmount = Integer.parseInt(((Button) actionEvent.getSource()).getId().split("Dollar")[1]);
            if(engine.getModel().getCurrentBet() == 0) {
                setText(engine.getModel().getCurrentPlayer().getName() + " bet $" + selectedAmount);
            } else {
                setText(engine.getModel().getCurrentPlayer().getName() + " raised by $" + selectedAmount);
            }

            engine.placeBet(selectedAmount);
            endOfTurn();
        }
    }

    public void discardCards(ActionEvent actionEvent) {
        engine.discardCards();
        endOfTurn();
        setText("Discarded! \nNext up is " + engine.getModel().getCurrentPlayer().getName() );
    }
    private void resetGame() {
        engine.resetGame();
        updateBankDisplay();
        updateBetDisplay();
        //clear card container
        CurrPlayerHandContainer.getChildren().clear();
        showDiscardBtn(false);
        showBetRaiseBtns(false);
        //disable all control buttons
        showControlBtns(new boolean[]{false, false, false, false, false});

        //show Ante up btn
        StartTurnBtn.setText("Ante Up");
        StartTurnBtn.setVisible(true);
    }

    public void playTurn(ActionEvent actionEvent) {
        if(StartTurnBtn.getText().equals("Ante Up")) {
            anteUp();
            engine.distributeCards();
            engine.getModel().setGameStage(GameStage.DISCARD);
        }
        else if(StartTurnBtn.getText().equals("Start Turn")) {
            startOfTurn();
        }
        else {
            //End of game. Reset game on next btn click
            resetGame();
            updatePlayerDisplays();
        }
    }
    private void anteUp() {
        //engine ante up
        engine.anteUp();
        //Set up game
        endOfTurn();

        //update display
        updatePlayerDisplays();
        setText("Each player has placed their ante\nand has received 5 cards");
        updateBankDisplay();
        //change button
        StartTurnBtn.setText("Start Turn");
    }

    //Helper method to get image
    private ImageView loadImageViewFromPath(String path) {
        float height = 930.0f * 0.11f;
        float width = 655.0f * 0.11f;
        Image image = new Image(Objects.requireNonNull(getClass().getResource(path)).toExternalForm(), width, height, false, false);
        ImageView imageView = null;
        imageView = new ImageView(image); // creates an image view from loaded image
        return imageView;
    }


    private void endOfTurn() {
        updateBankDisplay();
        updateBetDisplay();

        hideCards();
        showDiscardBtn(false);
        showBetRaiseBtns(false);
        //disable all control buttons
        showControlBtns(new boolean[]{false, false, false, false, false});

        //show next turn btn
        StartTurnBtn.setVisible(true);


        if(engine.getModel().getGameStage() == GameStage.BET) {
            //if bet phase is over, check for winner
            if(engine.checkIfBetPhaseIsOver()) {
                StartTurnBtn.setText("Play Again");
                Player winner = engine.determineWinner();
                setText(winner.getName() + " is the winner!");
            }
        }

        updatePlayerDisplays();

    }


    private void startOfTurn() {
        showCards();
        // Hide start turn button
        StartTurnBtn.setVisible(false);

        if(engine.getModel().getGameStage() == GameStage.DISCARD) {
            setText("It is " + engine.getModel().getCurrentPlayer().getName() + "'s turn\nSelect cards you want to discard and then press 'discard'");
            showDiscardBtn(true);
        }

        //if there is no bet and we are in the bet phase, force the first person to make a bet
        else if(engine.getModel().getGameStage() == GameStage.BET) {
            if (engine.getModel().getCurrentBet() == 0) {
                setText("It is " + engine.getModel().getCurrentPlayer().getName() + "'s turn\nStart by making a bet");
                showControlBtns(new boolean[]{true, false, false, false, false});
            }
            //if there is a bet, let the player do anything except for make a new bet
            else {
                setText("It is " + engine.getModel().getCurrentPlayer().getName() + "'s turn\nRaise, Go All In, Fold or Call to continue");
                showControlBtns(new boolean[]{false, true, true, true, true});
            }
        }
    }




    // show backs of cards. Used when in-between turns
    private void hideCards() {
        //clear card container
        CurrPlayerHandContainer.getChildren().clear();
        //display 5 card backs
        for(int i = 0; i < 5; i++) {
            CurrPlayerHandContainer.getChildren().add(loadImageViewFromPath("/images/cards/PNG/Card_Back.png"));
        }
    }
    //Show face of cards when player has started their turn
    private void showCards() {
        //clear card container
        CurrPlayerHandContainer.getChildren().clear();
        //display current players cards
        for (Card card: engine.getModel().getCurrentPlayer().getHand()) {
            CurrPlayerHandContainer.getChildren().add(card.getImageView());
        }
        //If we are in the discard stage, add listeners to cards
        if(engine.getModel().getGameStage() == GameStage.DISCARD) {
            for(int i = 0; i < CurrPlayerHandContainer.getChildren().size(); i++) {
                CurrPlayerHandContainer.getChildren().get(i).setId("PlayerCard" + i);
                ((ImageView) CurrPlayerHandContainer.getChildren().get(i)).setOnMouseClicked(selectCardHandler);
            }
            updateCardDisplay();
        }
        //If the discard stage is over, make all cards fully visible
        if(engine.getModel().getGameStage() == GameStage.BET) {
            for(int i = 0; i < CurrPlayerHandContainer.getChildren().size(); i++) {
                CurrPlayerHandContainer.getChildren().get(i).setStyle("-fx-opacity: 1;");
            }
        }
    }

    //Event handler for clicking on card
    EventHandler<MouseEvent> selectCardHandler = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            int indexOfCardClicked = Integer.parseInt((((ImageView) event.getSource()).getId()).split("PlayerCard")[1]);
            engine.getModel().changeCardToDiscard(indexOfCardClicked);
            updateCardDisplay();
            event.consume();
        }
    };

    private void showDiscardBtn(boolean showButton) {
        DiscardBtn.setDisable(!showButton);
    }

    private void updateBankDisplay() {
        PoolMoneyDisplay.setText("Cash Pool: $" + engine.getModel().getMoneyPool());
    }
    private void updateBetDisplay() {
        BetMoneyDisplay.setText("Bet: $" + engine.getModel().getCurrentBet());
    }
    private void updateCardDisplay() {
        //Only let listeners do something if in discard stage
        if(engine.getModel().getGameStage() == GameStage.DISCARD) {
            for (int i = 0; i < CurrPlayerHandContainer.getChildren().size(); i++) {
                if (engine.getModel().getCardsToDiscard()[i]) {
                    //add border
                    CurrPlayerHandContainer.getChildren().get(i).setStyle("-fx-opacity: 1;");
                } else {
                    //remove border
                    CurrPlayerHandContainer.getChildren().get(i).setStyle("-fx-opacity: 0.7;");
                }
            }
        }
    }

    //Enable or disable the bet/raise buttons (1,5,10,20,50,100)
    private void showBetRaiseBtns(boolean showButtons) {
        for(Node node :BetButtonContainer.getChildren()) {
            ((Button) node).setDisable(!showButtons);
        }
    }


    //Show control buttons
    //takes in array of boolean values representing whether the button should be enabled or not
    private void showControlBtns(boolean[] displayButtonsArray) {
        Button[] controlButtons = new Button[]{BetControlBtn, RaiseControlBtn, AllInControlBtn, FoldControlBtn, CallControlBtn};
        for(int i = 0; i < controlButtons.length; i++) {
            controlButtons[i].setDisable(!displayButtonsArray[i]);
        }
    }




    //Event listeners for turn buttons
    public void betTurn(ActionEvent actionEvent) {
        showBetRaiseBtns(true);
    }

    public void raiseTurn(ActionEvent actionEvent) {
        showBetRaiseBtns(true);

    }

    public void allInTurn(ActionEvent actionEvent) {
        setText(engine.getModel().getCurrentPlayer().getName() + " went all in!");
        engine.goAllIn();
        endOfTurn();
    }

    public void foldTurn(ActionEvent actionEvent) {
        setText(engine.getModel().getCurrentPlayer().getName() + " folded");
        engine.fold();
        endOfTurn();

    }

    public void callTurn(ActionEvent actionEvent) {
        setText(engine.getModel().getCurrentPlayer().getName() + " called");
        engine.call();
        endOfTurn();

    }
}
