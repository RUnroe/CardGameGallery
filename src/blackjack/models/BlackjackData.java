package blackjack.models;

import models.Deck;
import models.Player;

import java.io.Serializable;

public class BlackjackData implements Serializable {
    private Player[] players;
    private Player house;
    private Deck deck;
    private double priceOfHand;
    private int numberOfTurns;
    boolean isBetweenRounds;

    public BlackjackData(Player[] players, Player house, Deck deck, double priceOfHand, int numberOfTurns, boolean isBetweenRounds) {
        this.players = players;
        this.house = house;
        this.deck = deck;
        this.priceOfHand = priceOfHand;
        this.numberOfTurns = numberOfTurns;
        this.isBetweenRounds = isBetweenRounds;
    }

    public Player[] getPlayers() {
        return players;
    }

    public void setPlayers(Player[] players) {
        this.players = players;
    }

    public Player getHouse() {
        return house;
    }

    public void setHouse(Player house) {
        this.house = house;
    }

    public Deck getDeck() {
        return deck;
    }

    public void setDeck(Deck deck) {
        this.deck = deck;
    }

    public double getPriceOfHand() {
        return priceOfHand;
    }

    public void setPriceOfHand(double priceOfHand) {
        this.priceOfHand = priceOfHand;
    }

    public int getNumberOfTurns() {
        return numberOfTurns;
    }

    public void setNumberOfTurns(int numberOfTurns) {
        this.numberOfTurns = numberOfTurns;
    }

    public boolean isBetweenRounds() {
        return isBetweenRounds;
    }

    public void setBetweenRounds(boolean betweenRounds) {
        isBetweenRounds = betweenRounds;
    }
}
