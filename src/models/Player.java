package models;

import java.util.ArrayList;

public class Player {
    private String name;
    private int score;
    private ArrayList<Card> hand;
    private boolean isPlayerAI = false;

    public Player(String name, int score) {
        this.name = name;
        this.score = score;
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

    public ArrayList<Card> getHand() {
        return hand;
    }

    public void setHand(ArrayList<Card> hand) {
        this.hand = hand;
    }
    public void addToHand(Card card){
        hand.add(card);
    }
    public void removeFromHand(Card card){
        hand.remove(card);
    }
    public void removeFromHand(int index){
        try{
            hand.remove(index);
        } catch (IndexOutOfBoundsException e){}
    }

    public boolean isPlayerAI() {
        return isPlayerAI;
    }

    public void setPlayerAI(boolean playerAI) {
        isPlayerAI = playerAI;
    }
}
