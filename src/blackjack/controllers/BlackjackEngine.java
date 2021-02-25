package blackjack.controllers;

import blackjack.presenters.BlackjackGameScene;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import models.Card;
import models.CardComparator;
import models.Deck;
import models.Player;
import sample.Main;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

public class BlackjackEngine {
    int numOfPlayers;
    Player[] players;
    Player player;
    FXMLLoader fxmlLoader;
    BlackjackGameScene blackjackGameScene;

    public void setupGui() {
        fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("../views/blackjack-game-scene.fxml"));
        try {
            Parent parent = fxmlLoader.load();
            Main.getPrimaryStage().setScene(new Scene(parent));
            Main.getPrimaryStage().setX(100);
            Main.getPrimaryStage().setY(5);
            Main.getPrimaryStage().setResizable(false);
            Main.getPrimaryStage().setTitle("Blackjack");
        } catch (IOException e) {
            e.printStackTrace();
        }
        blackjackGameScene = fxmlLoader.getController(); // loads active controller from fxml
//        blackjackGameScene.exampleDisplayHand(players[0]);

    }

    public void setupEngine() {
        numOfPlayers = 5;
        players = new Player[numOfPlayers];
        for (int i = 0; i < numOfPlayers; i++) {
            players[i] = new Player("Player " + i, 20.00);
        }
//        player = new Player("Don");
    }

    public void setup() {
        setupEngine();
        setupGui();
        addListenersToEachPlayerHand();
//        blackjackGameScene.exampleDisplayHand(players[0]);
        setupStartingHands(new Deck(1));
//        blackjackGameScene.getImageViews().forEach(imageView -> {
//            System.out.println("src url: " + imageView.getImage().getUrl());
//        });
    }

    /**
     * creates listeners to update display every time a player hand is changed
     */
    private void addListenersToEachPlayerHand() {
        for (int i = 0; i < players.length; i++) {
            int finalI = i;
            players[i].getHand().addListener(new ListChangeListener<Card>() {
                @Override
                public void onChanged(Change<? extends Card> change) {
                    ObservableList<Node> playerFieldChildren = blackjackGameScene.anchorPanePlayerField.getChildren();
                    if (playerFieldChildren.get(finalI) instanceof HBox) { // checks for HBox before casting
                        HBox hBoxOfCurrentPlayer = (HBox) playerFieldChildren.get(finalI);
                        while (change.next()) { // checks for change
//                            System.out.println("Change: " + change);
                            if (change.wasAdded()) { // checks for additions to the hand
                                change.getAddedSubList().forEach(card -> {
                                    System.out.println(hBoxOfCurrentPlayer.getId() + " adding");
                                    hBoxOfCurrentPlayer.getChildren().add(card.getImageView()); // adds cards to display
                                });
                            } else if (change.wasRemoved()) { // checks for removal from hand
                                change.getAddedSubList().forEach(card -> {
                                    hBoxOfCurrentPlayer.getChildren().remove(card.getImageView()); // removes cards from display
                                });
                            } else if (change.wasPermutated()) {
                                hBoxOfCurrentPlayer.getChildren().clear();
                                change.getList().forEach(card -> {
                                    System.out.println("spacing: " + hBoxOfCurrentPlayer.getSpacing());
                                    hBoxOfCurrentPlayer.getChildren().add(card.getImageView()); // adds cards to display
                                });
                            }
                        }
//                        hBoxOfCurrentPlayer.getChildren().forEach(node -> System.out.println("Node: " + node));
                    }
                    blackjackGameScene.hBoxPlayerOneHand.getChildren().forEach(System.out::println);
                }
            });
            rotateCardsInHand(i);
        }
    }

    private void setupStartingHands(Deck deck) {
//        player = players[0];
        for (int i = 0; i < 5; i++) {
            for (Player player : players) {
                player.drawFromDeck(deck);
                player.getHand().sort(new CardComparator());
                System.out.println(player.getName() + "'s hand sorted by rank: ");
                int finalI = i;
                player.getHand().forEach(card -> {
                    if (finalI > 0) {
                        card.flip();
                    }
                    System.out.println('\t' + card.getName());
                });
                System.out.println("---------------------------");
            }
        }
    }

    private void rotateCardsInHand(int playerIndex) {
        HBox currentHbox = (HBox) blackjackGameScene.anchorPanePlayerField.getChildren().get(playerIndex);
        AtomicInteger translation = new AtomicInteger(0);
        AtomicInteger rotation = new AtomicInteger(-2 * currentHbox.getChildren().size());
        currentHbox.getChildren().forEach(child -> {
            child.setTranslateX(translation.doubleValue());
            child.setRotate(rotation.doubleValue());
            translation.getAndAdd(-2);
            rotation.getAndAdd(12);
        });
    }
}
