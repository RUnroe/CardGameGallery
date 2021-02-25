package models;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private String name;
    private int score;
    private List<Card> hand;

    public Player(String name, int score) {
        this.name = name;
        this.score = score;
    }

    public Player(String name) {
        this.name = name;
        this.hand = new ArrayList<>();
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
    public void addToScore(int add){
        score += add;
    }

    public List<Card> getHand() {
        return hand;
    }

    public void setHand(List<Card> hand) {
        this.hand = hand;
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
            hand = new ArrayList<>();
        }
        hand.add(card);
        deck.removeTopCard();
    }
}
