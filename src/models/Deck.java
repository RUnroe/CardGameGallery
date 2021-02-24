package models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Deck {
    ArrayList<Card> cards;

    public Deck(){
        createFullDeck();
    }

    private void createFullDeck(){
        cards = new ArrayList<>();
        for(int i = 1; i < 14; i++){
            cards.add(new Card(Suit.CLUBS, i));
            cards.add(new Card(Suit.DIAMONDS, i));
            cards.add(new Card(Suit.HEARTS, 1));
            cards.add(new Card(Suit.SPADES, 1));
        }
        Collections.shuffle(cards, new Random());
    }

    public ArrayList<Card> getCards() {
        return cards;
    }
    public Card getCardAt(int pos){
        return cards.get(pos);
    }

    public void shuffle(){
        Collections.shuffle(cards, new Random());
    }
}
