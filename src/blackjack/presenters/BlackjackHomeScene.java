package blackjack.presenters;

import blackjack.controllers.BlackjackEngine;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import models.Player;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class BlackjackHomeScene {
    public TextField Player1NameInput;
    public TextField Player2NameInput;
    public TextField Player3NameInput;
    public TextField Player4NameInput;
    public TextField Player5NameInput;
    public Spinner<Integer> numberOfPlayersInput;

    Stage stage;
    BlackjackEngine engine;

    private int numberOfPlayers;

    public void createGame(ActionEvent actionEvent) {
        //Create and instantiate player array list
//        ArrayList<Player> playerList = getPlayerList();
        //create engine
//        engine = new PokerEngine(playerList);
        //change scene to game scene
//        changeScene((Stage) CreateGameBtn.getScene().getWindow(), "../views/poker-game-scene.fxml");
       int numOfPlayers = numberOfPlayersInput.getValue();
        System.out.println(numOfPlayers);
        String[] playerNames = new String[] {Player1NameInput.getText(), Player2NameInput.getText(),
                Player3NameInput.getText(), Player4NameInput.getText(), Player5NameInput.getText()};
        double initialBankAmount = 20.00;
        getEngine().createPlayers(numOfPlayers, playerNames, initialBankAmount);
        if (getEngine().getHouse() == null) {
            getEngine().setHouse(new Player("House", Double.MAX_VALUE, true));
        }
//        for (int playerNum = 0; playerNum < getEngine().getPlayers().length; playerNum++) {
//            getEngine().getPlayers()[playerNum].setName(playerNames[playerNum]);
//        }
//        changeScene("../views/blackjack-game-scene.fxml");
        getEngine().setupGameGui(true);
    }

    public void changeScene(String fxmlPath) {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(fxmlPath)));
            getStage().setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Stage getStage() {
        if (stage == null) {
            stage = new Stage();
        }
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public BlackjackEngine getEngine() {
        return engine;
    }

    public void setEngine(BlackjackEngine engine) {
        this.engine = engine;
    }

    public void findFile(ActionEvent actionEvent) {
        throw new UnsupportedOperationException("This feature is not yet implemented.");
    }

    public void loadGame(ActionEvent actionEvent) {
//        throw new UnsupportedOperationException("This feature is not yet implemented.");
        String file = null;
        if (file != null) {

            int numOfPlayers = numberOfPlayersInput.getValue();
            System.out.println(numOfPlayers);
            String[] playerNames = new String[] {Player1NameInput.getText(), Player2NameInput.getText(),
                    Player3NameInput.getText(), Player4NameInput.getText(), Player5NameInput.getText()};
            double initialBankAmount = 20.00;
            getEngine().createPlayers(numOfPlayers, playerNames, initialBankAmount);
            if (getEngine().getHouse() == null) {
                getEngine().setHouse(new Player("House", Double.MAX_VALUE, true));
            }
        }
//        for (int playerNum = 0; playerNum < getEngine().getPlayers().length; playerNum++) {
//            getEngine().getPlayers()[playerNum].setName(playerNames[playerNum]);
//        }
//        changeScene("../views/blackjack-game-scene.fxml");
        getEngine().setupGameGui(true);
    }

    //Runs on startup. Adds event listener to spinner
    public void initialize() {
        //Set event listener on spinner
        try {
            numberOfPlayersInput.getEditor().textProperty().addListener((obs, oldValue, newValue) ->
                    changeNumberOfPlayersInput(Integer.parseInt(newValue)));
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    //Callback when changing the spinner for number of players
    public void changeNumberOfPlayersInput(int numberOfPlayers) {
        this.numberOfPlayers = numberOfPlayers;
        switch (numberOfPlayers) {
            case 1 -> {
                Player2NameInput.setDisable(true);
                Player3NameInput.setDisable(true);
                Player4NameInput.setDisable(true);
                Player5NameInput.setDisable(true);
            }
            case 2 -> {
                Player2NameInput.setDisable(false);
                Player3NameInput.setDisable(true);
                Player4NameInput.setDisable(true);
                Player5NameInput.setDisable(true);
            }
            case 3 -> {
                Player2NameInput.setDisable(false);
                Player3NameInput.setDisable(false);
                Player4NameInput.setDisable(true);
                Player5NameInput.setDisable(true);
            }
            case 4 -> {
                Player2NameInput.setDisable(false);
                Player3NameInput.setDisable(false);
                Player4NameInput.setDisable(false);
                Player5NameInput.setDisable(true);
            }
            case 5 -> {
                Player2NameInput.setDisable(false);
                Player3NameInput.setDisable(false);
                Player4NameInput.setDisable(false);
                Player5NameInput.setDisable(false);
            }
        }
//        adjustListForHouse();
    }

    private void openFile(File file) {
        Desktop desktop = Desktop.getDesktop();
        try {
            desktop.open(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
