package blackjack.models;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.FileInputStream;
import java.io.IOException;

public class BlackjackTemplateModel {
    public Card queenOfHearts;

    public void testCardImageView() {
        queenOfHearts = new Card("src/resources/images/cards/PNG/black/Hearts_Queen_black.png");
    }

    public class Card {
        public final ImageView imageView;

        public Card(String path) {
            imageView = loadImageViewFromPath(path);
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
    }
}
