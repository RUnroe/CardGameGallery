package models;

public class Card {
    private final Suit suit;
    private final int rank;
    //private image


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
        Suit[] suits = Suit.values();
        String name = "";
        switch(rank){
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
        String suit = "";
        for(int i = 0; i < 4; i++){
            if(this.suit == suits[i]){
                switch(i){
                    case 1:

                }
            }
        }

    }
}
