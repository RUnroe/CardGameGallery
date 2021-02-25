package poker.presenters;

import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;

public class PokerHomeScene {

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

    private int numberOfPlayers = 2;

    public void initialize() {
        //Set event listener on spinner
        NumberOfPlayersInput.getEditor().textProperty().addListener((obs, oldValue, newValue) ->
                changeNumberOfPlayersInput(Integer.parseInt(newValue)));
    }

    //Open choose file and let the user select a file
    public void findFile(ActionEvent actionEvent) {

    }

    //Go to game page with game information
    public void loadGame(ActionEvent actionEvent) {
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
    //Go to game page at start of game
    public void createGame(ActionEvent actionEvent) {
    }
}
