package models;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Objects;


public class Card {
    private final String name;
    private final ERank rank;
    private final ESuit suit;
    private final ImageView imageView;
    private final ImageView imageViewBack;
    private boolean isFlipped;

    public Card(ERank rank, ESuit suit) {
        this.rank = rank;
        this.suit = suit;
        this.name = getRankAsString() + " of " + getSuitAsString();
        this.imageView = loadImageViewFromPath("/images/cards/PNG/black/" + getSuitAsString() + "_" + getRankAsString() + "_black.png");
        this.imageViewBack = loadImageViewFromPath("/images/cards/PNG/Card_Back.png");
//        String url = "src/resources/images/cards/PNG/black/" + getSuitAsString() + "_" + getRankAsString() + "_black.png";
//        this.imageView = loadImageViewFromPath(url);
//        setupImageView();
        this.isFlipped = false;
    }

    private void setupImageView() {
        // image is too large, so it is being shrunk to 1/10th of it's native size while maintaining resolution
//        this.imageView.setScaleX(0.2);
//        this.imageView.setScaleY(0.2);
        // gives an id to keep from being considered duplicates and for later accessibility
        this.imageView.setId("imgView" + getRankAsString() + getSuitAsString());
        this.imageView.setVisible(true);
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

    public String getName() {
        return name;
    }

    /**
     * Returns user-friendly string representation of rank.
     */
    public String getRankAsString() {
        return rank.toString().toUpperCase().charAt(0) + rank.toString().substring(1).toLowerCase();
    }

    /**
     * Returns user-friendly string representation of suit.
     */
    public String getSuitAsString() {
        return suit.toString().toUpperCase().charAt(0) + suit.toString().substring(1).toLowerCase();
    }

    public ImageView getImageView() {
        return isFlipped() ? imageView : imageViewBack;
    }

    public ImageView getImageViewBack() {
        return imageViewBack;
    }

    private Image loadImageFromPath(String path) {
        Image image = null;
//        try {
//            URL url = getClass().getResource("images/cards/PNG/black/Clubs_Ace_black.png");
//            System.out.println("path: " + path);
//            FileInputStream fileInputStream = new FileInputStream(path); // opens an input stream on the path
        float height = 930.0f * 0.11f;
        float width = 655.0f * 0.11f;
        image = new Image(Objects.requireNonNull(getClass().getResource(path)).toExternalForm(), width, height, false, false); // loads an image from the input stream
//            System.out.println("image: " + image.getUrl());
//        } catch (IOException e) {
////                e.printStackTrace();
//            System.err.println("Image could not be loaded: File not found at " + path);
//        }
        return image;
    }

    public ImageView loadImageViewFromPath(String path) {
        Image image = loadImageFromPath(path);
        ImageView imageView = null;
        imageView = new ImageView(image); // creates an image view from loaded image
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
