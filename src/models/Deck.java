package models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Deck {
    ArrayList<Card> cards;

    public Deck(int i){
        switch(i){
            case 0:
                createFullDeck();
        }
    }

    private void createFullDeck(){
        cards = new ArrayList<>();
        for(int i = 1; i < 14; i++){
            cards.add(new Card(Suit.CLUBS, i));
            cards.add(new Card(Suit.DIAMONDS, i));
            cards.add(new Card(Suit.HEARTS, i));
            cards.add(new Card(Suit.SPADES, i));
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
    public void setCards(ArrayList<Card> cards){
        this.cards = cards;
    }
    public void removeCard(int index){
        cards.remove(index);
    }
}
