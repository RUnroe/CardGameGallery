package models;

import javax.swing.text.html.ImageView;

public class Card {
    private final String name;
    private final ESuit suit;
    private final ERank rank;
    private ImageView imageView;

    public Card(ESuit suit, ERank rank) {
        this.suit = suit;
        this.rank = rank;
        this.name = getRankAsString() + " of " + getSuitAsString();
    }

    public ESuit getSuit() {
        return suit;
    }

    /**
     * Returns the numerical value of the card's suit.
     * Useful for comparison when ranks are equivalent.
     */
    public int getSuitValue() {
        return suit.getValue();
    }

    public ERank getRank() {
        return rank;
    }

    /**
     * Returns the numerical value of the card's rank.
     * Useful for comparison; takes precedence over suit.
     */
    public int getRankValue() {
        return rank.getValue();
    }

    public String getName() {
        return name;
    }

    /**
     * Returns user-friendly string representation of rank.
     */
    public String getRankAsString() {
        return switch (rank) {
            case ACE -> "Ace";
            case TWO -> "Two";
            case THREE -> "Three";
            case FOUR -> "Four";
            case FIVE -> "Five";
            case SIX -> "Six";
            case SEVEN -> "Seven";
            case EIGHT -> "Eight";
            case NINE -> "Nine";
            case TEN -> "Ten";
            case JACK -> "Jack";
            case QUEEN -> "Queen";
            case KING -> "King";
        };
    }

    /**
     * Returns user-friendly string representation of suit.
     */
    public String getSuitAsString() {
        return switch (suit) {
            case DIAMONDS -> "Diamonds";
            case CLUBS -> "Clubs";
            case HEARTS -> "Hearts";
            case SPADES -> "Spades";
        };
    }
}
