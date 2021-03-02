package poker.models;

import models.Deck;
import models.Player;

import java.util.ArrayList;

public class PokerModel {


    private ArrayList<Player> playerList = new ArrayList<>();
    private ArrayList<Player> playersWhoHaveNotFolded = new ArrayList<>();
    private Deck deck = new Deck(0); //I have no clue why the deck class doesn't have a default constructor...
    private int moneyPool = 0;
    private int currentPlayerIndex = 0;
    private int currentBet;
    private int lastPlayerToRaise = -1;
    private GameStage gameStage = GameStage.ANTE;


    //Getters and Setters
    public ArrayList<Player> getPlayerList() {
        return playerList;
    }

    public void setPlayerList(ArrayList<Player> playerList) {
        this.playerList = playerList;
    }

    public void addPlayerToPlayerList(Player player) {
        this.playerList.add(player);
    }

    public ArrayList<Player> getPlayersWhoHaveNotFolded() {
        return this.playersWhoHaveNotFolded;
    }

    public void resetPlayersWhoHaveNotFolded() {
        this.playersWhoHaveNotFolded = (ArrayList<Player>) this.playerList.clone();
    }

    public void foldPlayerByIndex(int indexOfPlayer) {
        this.playersWhoHaveNotFolded.remove(indexOfPlayer);
    }

    public Deck getDeck() {
        return this.deck;
    }

    public void resetDeck() {
        this.deck = new Deck(0);
    }

    public int getMoneyPool() {
        return this.moneyPool;
    }

    public void setMoneyPool(int moneyPool) {
        this.moneyPool = moneyPool;
    }
    public void addToMoneyPool(int amountToAdd) {
        this.moneyPool += amountToAdd;
    }


    public int getCurrentPlayerIndex() {
        return this.currentPlayerIndex;
    }

    public void setCurrentPlayerIndex(int currentPlayerIndex) {
        this.currentPlayerIndex = currentPlayerIndex;
    }

    public Player getCurrentPlayer() {return playerList.get(currentPlayerIndex);}

    public Player getNextPlayer(int numOfPlayersAfterCurrent) {
        int playerIndex = (currentPlayerIndex + numOfPlayersAfterCurrent) % playerList.size();
        return playerList.get(playerIndex);
    }

    //By default, switch by 1. We do not want to increment the playerIndex when a player folds(is removed from arraylist)
    public void switchTurn() {
        switchTurn(1);
    }
    public void switchTurn(int incrementValue) {
        this.currentPlayerIndex += incrementValue;
        //If next player index is out of bounds, wrap back to player 0
        this.currentPlayerIndex %= getNumberOfActivePlayers();
    }

    public int getCurrentBet() {
        return currentBet;
    }

    public void setCurrentBet(int currentBet) {
        this.currentBet = currentBet;
    }

    public void raiseCurrentBet(int amount) {
        this.currentBet += amount;
    }

    public int getNumberOfActivePlayers() {
        return this.playersWhoHaveNotFolded.size();
    }


    public int getLastPlayerToRaise() {
        return lastPlayerToRaise;
    }

    public void setLastPlayerToRaise(int lastPlayerToRaise) {
        this.lastPlayerToRaise = lastPlayerToRaise;
    }

    public GameStage getGameStage() {
        return gameStage;
    }

    public void setGameStage(GameStage gameStage) {
        this.gameStage = gameStage;
    }

}
