package models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Deck implements Serializable {
    ArrayList<Card> cards;

    public Deck(int numOfCardPacks) {
//        switch(i){
//            case 0:
//                createFullDeck();
//        }
        createDeck(numOfCardPacks);
    }

    private void createFullDeck() {
        cards = new ArrayList<>();
        for (int i = 1; i < 14; i++) {
            cards.add(new Card(ERank.values()[i], ESuit.DIAMONDS));
            cards.add(new Card(ERank.values()[i], ESuit.CLUBS));
            cards.add(new Card(ERank.values()[i], ESuit.HEARTS));
            cards.add(new Card(ERank.values()[i], ESuit.SPADES));
        }
        Collections.shuffle(cards, new Random());
    }

    private void createDeck(int numOfCardPacks) {
        cards = new ArrayList<>();
        for (int i = 0; i < numOfCardPacks; i++) {
            for (int rankValue = 1; rankValue < 14; rankValue++) {
                cards.add(new Card(ERank.values()[rankValue - 1], ESuit.DIAMONDS));
                cards.add(new Card(ERank.values()[rankValue - 1], ESuit.CLUBS));
                cards.add(new Card(ERank.values()[rankValue - 1], ESuit.HEARTS));
                cards.add(new Card(ERank.values()[rankValue - 1], ESuit.SPADES));
            }
            Collections.shuffle(cards, new Random());
        }
    }

    public ArrayList<Card> getCards() {
        return cards;
    }

    public Card getCardAt(int pos) {
        return cards.get(pos);
    }

    public void shuffle() {
        Collections.shuffle(cards, new Random());
    }

    public void setCards(ArrayList<Card> cards) {
        this.cards = cards;
    }

    public void removeCard(int index) {
        cards.remove(index);
    }

    public Card getTopCard() {

        return cards.get(cards.size() - 1);
    }

    public void removeTopCard() {
        cards.remove(cards.size() - 1);
    }
}
