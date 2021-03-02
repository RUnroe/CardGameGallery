package poker.presenters;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import models.Card;
import models.Player;
import poker.controllers.PokerEngine;

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
    public Label WorkingMoneyDisplay;
    public Label BetMoneyDisplay;
    public Label PoolMoneyDisplay;
    public Label OutputTxt;
    public Button DiscardBtn;

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
    }

    public void discardCards(ActionEvent actionEvent) {
    }

    public void playTurn(ActionEvent actionEvent) {
        if(StartTurnBtn.getText().equals("Ante Up")) {
            anteUp();
        }
        else {

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
        hideCards();
        disableBetRaiseBtns();
        disableControlBtns();
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
    }

    //Disable the bet/raise buttons (1,5,10,20,50,100)
    private void disableBetRaiseBtns() {

    }
    //Enable the bet/raise buttons
    private void enableBetRaiseBtns() {

    }

    //Disable control buttons
    private void disableControlBtns() {

    }

    //Enable control buttons. Allow user to play their turn
    private void enableControlBtns() {

    }

}
