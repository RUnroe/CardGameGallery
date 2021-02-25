package poker.controllers;

import models.Player;
import poker.models.PokerModel;

import java.util.ArrayList;

public class PokerEngine {
    private int bankInitValue = 100;
    private PokerModel model = new PokerModel();


    public PokerEngine(String[] playerNames, boolean playingAgainstAI) {
        //Get initial data
        ArrayList<Player> playerList = new ArrayList<>();
        for(int i = 0; i < playerNames.length; i++) {
            Player player = new Player(playerNames[i], bankInitValue);
            playerList.add(player);
        }
        int numOfPlayers = playerList.size();
        if(playingAgainstAI) playerList.get(numOfPlayers-1).setPlayerAI(true);

        //set initial data in the model
        model.setPlayerList(playerList);
        model.resetPlayersWhoHaveNotFolded();
    }

}
