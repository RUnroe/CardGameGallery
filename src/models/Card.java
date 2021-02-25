package models;

import javax.swing.text.html.ImageView;

public class Card {
    private final Suit suit;
    private final int rank;
    private ImageView image;

    public Card(Suit suit, int rank) {
        this.suit = suit;
        this.rank = rank;
    }

    public Suit getSuit() {
        return suit;
    }

    public int getRank() {
        return rank;
    }

    public String getName(){
        String name = "";
        switch(rank){
            case 1:
            case 14:
                name = "Ace";
                break;
            case 11:
                name = "Jack";
                break;
            case 12:
                name = "Queen";
                break;
            case 13:
                name = "King";
                break;
            default:
                name = "" + rank;
                break;
        }
        String suit = this.suit.toString();
        suit = suit.substring(0,1).toUpperCase()+suit.substring(1).toLowerCase();
        return name + " of " + suit;

    }
}
