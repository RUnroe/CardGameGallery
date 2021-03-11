package poker.models;

import models.Card;
import models.Deck;
import models.Player;

import java.io.Serializable;
import java.util.ArrayList;

public class PokerModel implements Serializable {


    private ArrayList<Player> playerList = new ArrayList<>();
    private Deck deck = new Deck(1);
    private int moneyPool = 0;
    private int currentPlayerIndex = 0;
    private int currentBet = 0;
    private int lastPlayerToRaise = -1;
    private GameStage gameStage = GameStage.ANTE;
    private final boolean[] cardsToDiscard = new boolean[] {false, false, false, false, false};
    private int raiseAmount = 0;
    private boolean inBetPhase = true;

    private final boolean[] playerHasFolded = new boolean[] {false, false, false, false};

    //Getters and Setters
    public ArrayList<Player> getPlayerList() {
        return playerList;
    }

    public void setPlayerList(ArrayList<Player> playerList) {
        this.playerList = playerList;
    }

    public void clearPlayersHands() {
        for (Player player: playerList) {
            player.setHand(new ArrayList<Card>());
        }
    }

    public void addPlayerToPlayerList(Player player) {
        this.playerList.add(player);
    }

    public Deck getDeck() {
        return this.deck;
    }

    public void resetDeck() {
        this.deck = new Deck(1);
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
        this.currentPlayerIndex += 1;
        //If next player index is out of bounds, wrap back to player 0
        this.currentPlayerIndex %= playerList.size();
        //If the new player has folded, go to the next player
        if(playerHasFolded[currentPlayerIndex]) switchTurn();
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
        return getPlayersWhoHaveNotFolded().size();
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



    public boolean[] getCardsToDiscard() {
        return cardsToDiscard;
    }
    public void resetCardsToDiscard() {
        for(int i = 0; i < 5; i++) {
            cardsToDiscard[i] = false;
        }
    }
    public void changeCardToDiscard(int position) {
        cardsToDiscard[position] = !cardsToDiscard[position];
    }



    public int getRaiseAmount() {
        return raiseAmount;
    }

    public void setRaiseAmount(int raiseAmount) {
        this.raiseAmount = raiseAmount;
    }


    public boolean isInBetPhase() {
        return inBetPhase;
    }

    public void setInBetPhase(boolean inBetPhase) {
        this.inBetPhase = inBetPhase;
    }



    public boolean[] getPlayerHasFolded() {
        return playerHasFolded;
    }
    public void foldPlayerByIndex(int indexOfPlayer) {
        this.playerHasFolded[indexOfPlayer] = true;
    }

    public void resetPlayerHasFolded() {
        for (int i = 0; i < playerHasFolded.length; i++) {
            playerHasFolded[i] = false;
        }
    }

    public ArrayList<Player> getPlayersWhoHaveNotFolded() {
        ArrayList<Player> players = new ArrayList<>();
        for(int i = 0; i < playerList.size(); i++) {
            if(!playerHasFolded[i]) players.add(playerList.get(i));
        }
        return players;
    }

}
