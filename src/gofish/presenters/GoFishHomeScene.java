package gofish.presenters;

import gofish.GoFish;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import models.Player;
import poker.controllers.PokerEngine;
import poker.models.PokerModel;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GoFishHomeScene {
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
    public CheckBox checkBoxIsPlayerOneAI;
    public CheckBox checkBoxIsPlayerThreeAI;
    public CheckBox checkBoxIsPlayerTwoAI;
    public CheckBox checkBoxIsPlayerFourAI;
    private CheckBox[] aiCheckboxes;



    private int numberOfPlayers = 2;
    private static PokerEngine engine;

    FileChooser fileChooser = new FileChooser();
    Desktop desktop = Desktop.getDesktop();
    File loadedFile;

    private boolean isPlayerOneAI = false;
    private boolean isPlayerTwoAI = false;
    private boolean isPlayerThreeAI = false;
    private boolean isPlayerFourAI = false;


    //Runs on startup. Adds event listener to spinner
    public void initialize() {
        //Set event listener on spinner
        try {
            NumberOfPlayersInput.getEditor().textProperty().addListener((obs, oldValue, newValue) ->
                    changeNumberOfPlayersInput(Integer.parseInt(newValue)));
        } catch(Exception e) {
            e.printStackTrace();
        }
        aiCheckboxes = new CheckBox[]{checkBoxIsPlayerOneAI, checkBoxIsPlayerTwoAI, checkBoxIsPlayerThreeAI, checkBoxIsPlayerFourAI};
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
//    public void quitGame(ActionEvent actionEvent) {
//        changeScene((Stage) QuitGameBtn.getScene().getWindow(), "../views/poker-home-scene.fxml");
//    }
    //Go to game page at start of game
    public void createGame(ActionEvent actionEvent) {
//        System.out.println("Num Of Players: " + numberOfPlayers);
        //Create and instantiate player array list
//        ArrayList<Player> playerList = getPlayerList();
        //create engine
//        engine = new PokerEngine(playerList);
        //change scene to game scene
//        changeScene((Stage) CreateGameBtn.getScene().getWindow(), "../views/poker-game-scene.fxml");
        List<Player> players = getPlayerList();
        for (int playerNum = 0; playerNum < players.size(); playerNum++) {
            players.get(playerNum).setPlayerAI(aiCheckboxes[playerNum].isSelected());
            System.out.println(players.get(playerNum).getName() + "is AI: " + players.get(playerNum).isPlayerAI());
        }
        GoFish.setRootStage((Stage) CreateGameBtn.getScene().getWindow());
        GoFish.setup(players.toArray(new Player[0]));
        if (GoFish.getPlayers()[0].isPlayerAI()) {
            GoFish.aiTurn();
        }
//        changeScene((Stage) CreateGameBtn.getScene().getWindow(), "../views/go-fish-game-scene.fxml");
    }
    //Go to game page with game information
    public void loadGame(ActionEvent actionEvent) {
        try {
            FileInputStream f = new FileInputStream(loadedFile);
            ObjectInputStream o = new ObjectInputStream(f);
            PokerModel e = (PokerModel) o.readObject();
            f.close();
            o.close();
            engine = new PokerEngine(e);
        } catch (Exception e){
            System.out.println(e);
        }

    }


    //Open choose file and let the user select a file
    public void findFile(ActionEvent actionEvent) {
        //Set extension filter for text files
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("PKR files (*.pkr)", "*.pkr");
        fileChooser.getExtensionFilters().add(extFilter);

        loadedFile = fileChooser.showOpenDialog(CreateGameBtn.getScene().getWindow());
        if (loadedFile != null) {
            openFile(loadedFile);
        }
        try {
            LoadedFileName.setText(loadedFile.getName());
        }catch(Exception e) {
            LoadedFileName.setText("No file chosen");
        }
    }


//    public void saveGame(ActionEvent actionEvent) {
//        //Set extension filter for text files
//        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("PKR files (*.pkr)", "*.pkr");
//        fileChooser.getExtensionFilters().add(extFilter);
//
//        //Show save file dialog
//        File file = fileChooser.showSaveDialog(QuitGameBtn.getScene().getWindow());
//
//        if (file != null) {
//            engine.saveBoard(file);
//        }
//    }


    private void openFile(File file) {
        try {
            desktop.open(file);
        } catch (IOException ex) {
        }
    }




    private ArrayList<Player> getPlayerList() {
        ArrayList<Player> playerList = new ArrayList<>();
        //Create new players and add them into list
        TextField[] playerNameInputs = new TextField[]{Player1NameInput, Player2NameInput, Player3NameInput, Player4NameInput};
        boolean[] playerAiBools = new boolean[]{isPlayerOneAI, isPlayerTwoAI, isPlayerThreeAI, isPlayerFourAI};
        for(int i = 0; i < numberOfPlayers; i++) {
            playerList.add(new Player(playerNameInputs[i].getText()));
            playerList.get(i).setPlayerAI(playerAiBools[i]);
        }
        //Make last player AI if user checked AI check box
//        int numOfPlayers = playerList.size();
//        if(PlayWithHouseCheckBox.isSelected()) playerList.get(numOfPlayers-1).setPlayerAI(true);
        return playerList;
    }

    //Callback when changing the spinner for number of players
    public void changeNumberOfPlayersInput(int numberOfPlayers) {
        this.numberOfPlayers = numberOfPlayers;
        switch (numberOfPlayers) {
            case 2 -> {
                Player3NameInput.setDisable(true);
                Player4NameInput.setDisable(true);
            }
            case 3 -> {
                Player3NameInput.setDisable(false);
                Player4NameInput.setDisable(true);
            }
            case 4 -> {
                Player3NameInput.setDisable(false);
                Player4NameInput.setDisable(false);
            }
        }
//        adjustListForHouse();
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



    //Helper method to get image
    private ImageView loadImageViewFromPath(String path) {
        float height = 930.0f * 0.11f;
        float width = 655.0f * 0.11f;
        Image image = new Image(Objects.requireNonNull(getClass().getResource(path)).toExternalForm(), width, height, false, false);
        ImageView imageView = null;
        imageView = new ImageView(image); // creates an image view from loaded image
        return imageView;
    }

    public void onActionIsPlayerOneAI(ActionEvent actionEvent) {
        isPlayerOneAI = checkBoxIsPlayerOneAI.isSelected();
        Player1NameInput.setText((isPlayerOneAI ? "CPU-1" : "Player One"));
    }

    public void onActionIsPlayerTwoAI(ActionEvent actionEvent) {
        isPlayerTwoAI = checkBoxIsPlayerTwoAI.isSelected();
        Player2NameInput.setText((isPlayerTwoAI ? "CPU-2" : "Player Two"));
    }

    public void onActionIsPlayerThreeAI(ActionEvent actionEvent) {
        isPlayerThreeAI = checkBoxIsPlayerThreeAI.isSelected();
        Player3NameInput.setText((isPlayerThreeAI ? "CPU-3" : "Player Three"));
    }

    public void onActionIsPlayerFourAI(ActionEvent actionEvent) {
        isPlayerFourAI = checkBoxIsPlayerFourAI.isSelected();
        Player4NameInput.setText((isPlayerFourAI ? "CPU-4" : "Player Four"));
    }





    public void backToMenu(ActionEvent actionEvent) {
        ((Stage) CreateGameBtn.getScene().getWindow()).close();
    }
}
