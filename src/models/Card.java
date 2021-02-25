package models;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.FileInputStream;
import java.io.IOException;


public class Card {
    private final String name;
    private final ESuit suit;
    private final ERank rank;
    private final ImageView imageView;
    private boolean isFlipped;

    public Card(ESuit suit, ERank rank) {
        this.suit = suit;
        this.rank = rank;
        this.name = getRankAsString() + " of " + getSuitAsString();
        this.imageView = loadImageViewFromPath("src/resources/images/cards/PNG/black/" + getSuitAsString() + "_" + getRankAsString() + "_black.png");
        this.isFlipped = false;
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

    public ImageView getImageView() {
        return imageView;
    }

    private Image loadImageFromPath(String path) {
        Image image = null;
        try {
            FileInputStream fileInputStream = new FileInputStream(path); // opens an input stream on the path
            image = new Image(fileInputStream); // loads an image from the input stream
        } catch (IOException e) {
//                e.printStackTrace();
            System.err.println("Image could not be loaded: File not found at " + path);
        }
        return image;
    }

    public ImageView loadImageViewFromPath(String path) {
        Image image = loadImageFromPath(path);
        ImageView imageView = null;
        if (image != null) {
            imageView = new ImageView(image); // creates an image view from loaded image
        } else {
            System.err.println("ImageView could not be loaded: source Image was null");
        }
        return imageView;
    }

    public boolean isFlipped() {
        return isFlipped;
    }

    public void setFlipped(boolean flipped) {
        isFlipped = flipped;
    }

    public void flip() {
        setFlipped(!isFlipped());
    }
}
