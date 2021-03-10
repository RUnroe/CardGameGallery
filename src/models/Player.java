package models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Player implements Serializable {
    private String name;
    private int score;
    private boolean isPlayerAI = false;
    private double bank;
    private ObservableList<Card> hand;

    public Player(String name, int score) {
        this.name = name;
        this.score = score;
    }

    public Player(String name, double initialBankAmount) {
        this.name = name;
        this.bank = initialBankAmount;
        this.hand = FXCollections.observableArrayList();
    }

    public Player(String name) {
        this.name = name;
        this.hand = FXCollections.observableArrayList();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void addToScore(int add) {
        score += add;
    }

    public double getBank() {
        return bank;
    }

    public void setBank(double bank) {
        this.bank = bank;
    }

    public ObservableList<Card> getHand() {
        return hand;
    }

    public void setHand(ObservableList<Card> hand) {
        this.hand = hand;
    }

    public void setHand(List<Card> hand) {
        this.hand = FXCollections.observableArrayList(hand);
    }

    public void addToHand(Card card) {
        hand.add(card);
    }
    public void removeFromHand(Card card){
        hand.remove(card);
    }

    public void removeFromHand(int index) {
        try {
            hand.remove(index);
        } catch (IndexOutOfBoundsException e) {
            System.err.println("IndexOutOfBoundsException: Couldn't remove card at index " + index
                    + " because max index is " + (hand.size() - 1));
        }
    }

    public void drawFromDeck(Deck deck) {
        int topOfDeckIndex = deck.getCards().size() - 1;
        Card card = deck.getTopCard();
        if (hand == null) {
            hand = FXCollections.observableArrayList();
        }
        hand.add(card);
        deck.removeTopCard();
    }
    public boolean isPlayerAI() {
        return isPlayerAI;
    }

    public void setPlayerAI(boolean playerAI) {
        isPlayerAI = playerAI;
    }
}
