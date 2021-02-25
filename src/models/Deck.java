package models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Deck {
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
            cards.add(new Card(ESuit.DIAMONDS, ERank.values()[i]));
            cards.add(new Card(ESuit.CLUBS, ERank.values()[i]));
            cards.add(new Card(ESuit.HEARTS, ERank.values()[i]));
            cards.add(new Card(ESuit.SPADES, ERank.values()[i]));
        }
        Collections.shuffle(cards, new Random());
    }

    private void createDeck(int numOfCardPacks) {
        cards = new ArrayList<>();
        for (int i = 0; i < numOfCardPacks; i++) {
            for (int rankValue = 1; rankValue < 14; rankValue++) {
                cards.add(new Card(ESuit.DIAMONDS, ERank.values()[rankValue - 1]));
                cards.add(new Card(ESuit.CLUBS, ERank.values()[rankValue - 1]));
                cards.add(new Card(ESuit.HEARTS, ERank.values()[rankValue - 1]));
                cards.add(new Card(ESuit.SPADES, ERank.values()[rankValue - 1]));
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
