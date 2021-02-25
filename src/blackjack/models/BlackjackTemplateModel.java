package blackjack.models;

import models.Card;

public class BlackjackTemplateModel {
    public Card queenOfHearts;

    public void testCardImageView() {
        String suitAsString;
        String rankAsString;
//        queenOfHearts = new Card(ESuit.SPADES, ERank.ACE);
//        switch (queenOfHearts.getRank()) {
//            case 11 -> rankAsString = "Jack";
//
//        }
        System.out.println(queenOfHearts.getName());
        System.out.println();
    }

    public void getBlackCardPath(Card card) {
        String cardPath = "src/resources/images/cards/PNG/black/" + card.getSuitAsString() + "_" + card.getRankAsString() + "_black.png";
    }

//    public class Card {
//        public final ImageView imageView;
//
//        public Card(String path) {
//            imageView = loadImageViewFromPath(path);
//        }
//
//        private Image loadImageFromPath(String path) {
//            Image image = null;
//            try {
//                FileInputStream fileInputStream = new FileInputStream(path); // opens an input stream on the path
//                image = new Image(fileInputStream); // loads an image from the input stream
//            } catch (IOException e) {
////                e.printStackTrace();
//                System.err.println("Image could not be loaded: File not found at " + path);
//            }
//            return image;
//        }
//
//        public ImageView loadImageViewFromPath(String path) {
//            Image image = loadImageFromPath(path);
//            ImageView imageView = null;
//            if (image != null) {
//                imageView = new ImageView(image); // creates an image view from loaded image
//            } else {
//                System.err.println("ImageView could not be loaded: source Image was null");
//            }
//            return imageView;
//        }
//    }
}
