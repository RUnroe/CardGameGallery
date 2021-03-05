package blackjack.controllers;

//region Imports
import blackjack.presenters.BlackjackGameScene;
import blackjack.presenters.BlackjackHomeScene;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import models.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

//endregion
public class BlackjackEngine {
    public static final String HOME_PATH = "../views/blackjack-home-scene.fxml";
    public static final String GAME_PATH = "../views/blackjack-game-scene.fxml";
    int numOfPlayers;
    int numOfTurns;
    Player[] players;
    Player house;
    FXMLLoader fxmlLoader;
//    BlackjackGameScene blackjackGameScene;
    Stage stage;
    static Deck deck;

    //region Setup Methods
    public void setupHomeGui() {
        changeScene(HOME_PATH);
        if (fxmlLoader.getController() instanceof BlackjackHomeScene) {
            BlackjackHomeScene blackjackHomeScene = fxmlLoader.getController();
            blackjackHomeScene.setStage(getStage());
            blackjackHomeScene.setEngine(this);
            getStage().setX(getStage().getX() - 500);
            getStage().setY(getStage().getY() - 150);
        }
    }
    public void setupGameGui() {
        getStage().setX(getStage().getX() - 75);
        getStage().setY(getStage().getY() - 20);
        changeScene(GAME_PATH);
        if (fxmlLoader.getController() instanceof BlackjackGameScene) {
            setupEngine();
            BlackjackGameScene blackjackGameScene = fxmlLoader.getController();
            blackjackGameScene.setStage(getStage());
            blackjackGameScene.setEngine(this);
            blackjackGameScene.setPlayerNames();
        }
    }

    public void setupEngine() {
        if (house == null) {
            house = new Player("House", Double.MAX_VALUE, true);
        }
        addListenersToEachPlayerHand();
        addListenerToHouseHand();
        setDeck(new Deck(1));
        setupStartingHands(getDeck());
//        for (int i = 0; i < numOfPlayers; i++) {
//            if (players[i] == null) {
//                players[i] = new Player("Player " + i, 20.00);
//            }
//        }
//        player = new Player("Don");
    }

    public void setup(Stage stage) {
//        setupEngine();
        numOfPlayers = 5;
        if (getPlayers() == null) {
            setPlayers(new Player[numOfPlayers]);
        }
        setStage(stage);
        setupHomeGui();
//        setupGameGui();
//        blackjackGameScene.exampleDisplayHand(players[0]);
    }
    //endregion

    /**
     * creates listeners to update display every time a player hand is changed
     * only call if blackjack-game-scene.fxml is loaded
     */
    private void addListenersToEachPlayerHand() {
        if (fxmlLoader.getController() instanceof BlackjackGameScene) {
            BlackjackGameScene blackjackGameScene = fxmlLoader.getController();
            for (int playerNum = 0; playerNum < players.length; playerNum++) {
                HBox hBoxOfCurrentPlayer = blackjackGameScene.getPlayerHandHBoxs()[playerNum];
                players[playerNum].getHand().addListener((ListChangeListener<Card>) change -> {
//                    BlackjackGameScene blackjackGameScene = fxmlLoader.getController();
//                    ObservableList<Node> playerFieldChildren = blackjackGameScene.anchorPanePlayerField.getChildren();
//                    if (playerFieldChildren.get(finalI) instanceof HBox) { // checks for HBox before casting
//                        HBox hBoxOfCurrentPlayer = (HBox) playerFieldChildren.get(finalI);
                        updateHboxOnListChange(change, hBoxOfCurrentPlayer);
//                        hBoxOfCurrentPlayer.getChildren().forEach(node -> System.out.println("Node: " + node));
//                    }
//                    blackjackGameScene.hBoxPlayerOneHand.getChildren().forEach(System.out::println);
                });
//            rotateCardsInHand(i);
            }
        }
    }

    private void addListenerToHouseHand() {
        if (fxmlLoader.getController() instanceof BlackjackGameScene) {
                house.getHand().addListener((ListChangeListener<Card>) change -> {
                    BlackjackGameScene blackjackGameScene = fxmlLoader.getController();
                    HBox hBoxHouseHand = blackjackGameScene.hBoxHouseHand;
                     // checks for HBox before casting
                    updateHboxOnListChange(change, hBoxHouseHand);
//                        hBoxOfCurrentPlayer.getChildren().forEach(node -> System.out.println("Node: " + node));

//                    blackjackGameScene.hBoxPlayerOneHand.getChildren().forEach(System.out::println);
                });
//            rotateCardsInHand(i);
            }

    }

    private void updateHboxOnListChange(ListChangeListener.Change<? extends Card> change, HBox hBox) {
        while (change.next()) { // checks for change
            if (change.wasAdded()) { // checks for additions to the hand
                change.getAddedSubList().forEach(card -> {
                    hBox.getChildren().add(card.getImageView()); // adds cards to display
                });
            } else if (change.wasRemoved()) { // checks for removal from hand
                change.getAddedSubList().forEach(card -> {
                    hBox.getChildren().remove(card.getImageView()); // removes cards from display
                });
            } else if (change.wasPermutated()) {
                hBox.getChildren().clear();
                change.getList().forEach(card -> {
                    hBox.getChildren().add(card.getImageView()); // adds cards to display
                });
            }
        }
    }

    private void setupStartingHands(Deck deck) {
        for (int numOfInitialCards = 0; numOfInitialCards < 2; numOfInitialCards++) {
            drawOnceForAllHands(deck);
        }
        sortAllHands();
        flipAllCardsExceptHoleCard();
    }

    private void drawOnceForAllHands(Deck deck) {
        Arrays.stream(players).forEach(player -> player.drawFromDeck(deck));
        house.drawFromDeck(deck);
    }

    private void flipCardAndUpdateDisplay(Player player, int cardIndex) {
        Card card = player.getHand().get(cardIndex);
        card.flip();
        int playerIndex = Arrays.stream(players).collect(Collectors.toList()).indexOf(player);
        if (fxmlLoader.getController() instanceof BlackjackGameScene) {
            BlackjackGameScene blackjackGameScene = fxmlLoader.getController();
            ImageView imageView = card.getImageView();
            // code below updates the source image of the image view at the passed-in player's indexed hbox
            // or the house's hbox
            // based on the passed-in card's current image view after the flip
            if (player.equals(house)) {
                ((ImageView) blackjackGameScene.hBoxHouseHand.getChildren().get(cardIndex)).setImage(imageView.getImage());
            } else {
                ((ImageView) blackjackGameScene.getPlayerHandHBoxs()[playerIndex].getChildren().get(cardIndex)).setImage(imageView.getImage());
            }
        }
    }

    private void flipCardAndUpdateDisplay(Player player, Card card) {
        flipCardAndUpdateDisplay(player, player.getHand().indexOf(card));
    }

    private void flipAllCardsExceptHoleCard() {
        Arrays.stream(players).forEach(player -> player.getHand().forEach(card -> flipCardAndUpdateDisplay(player, card)));
        flipCardAndUpdateDisplay(house, 0);
    }

    private void sortAllHands() {
        for (Player player : players) {
            player.getHand().sort(new CardComparator());
        }
        house.getHand().sort(new CardComparator());
    }


    /**
     * rotates cards to look more like being held in a hand
     * only call if blackjack-game-scene.fxml is loaded
     * @param playerIndex player that holds the cards to rotate
     */
    private void rotateCardsInHand(int playerIndex) {
        if (fxmlLoader.getController() instanceof BlackjackGameScene) {
            BlackjackGameScene blackjackGameScene = fxmlLoader.getController();
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

    public Player[] getPlayers() {
        return players;
    }

    public void setPlayers(Player[] players) {
        this.players = players;
    }

    public Stage getStage() {
        if (stage == null) {
            stage = new Stage();
        }
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void changeScene(String path) {
        try {
            fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource(path));
            Parent sceneRoot = fxmlLoader.load();
            getStage().setScene(new Scene(sceneRoot));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void createPlayers(int numOfPlayers, String[] names, double initialBankAmount) {
        setPlayers(new Player[numOfPlayers]);
        System.out.println(numOfPlayers);
        for (int playerNum = 0; playerNum < numOfPlayers; playerNum++) {
            System.out.println(Arrays.toString(names));
            getPlayers()[playerNum] = new Player(names[playerNum], initialBankAmount);
            System.out.println(getPlayers()[playerNum].getName());
        }
    }

    private void takePlayerTurn() {
        Player currentPlayer = players[getCurrentPlayerIndex()];

    }

    public void handleHit() {
        Player currentPlayer = players[getCurrentPlayerIndex()];
        currentPlayer.drawFromDeck(getDeck());
        if(hasEndTurnCondition(currentPlayer)) {
            passTurn();
        }
    }

    private boolean hasEndTurnCondition(Player currentPlayer) {
        return false;
    }




    private boolean hasLostOnTurn(Player player) {
        return calculateHandTotal(player) > 21 || player.getBank() <= -50.00;
    }

    private int calculateHandTotal(Player player) {
        int totalValue = player.getHandTotal();
        if (totalValue < 11) {
            int numOfAces = (int) player.getHand().stream().filter(card -> card.getRank().equals(ERank.ACE)).count();
            while (totalValue < 11 && numOfAces > 0) {
                totalValue += 10;
                numOfAces--;
            }
        }
        return totalValue;
    }

    private void passTurn() {
        setNextPlayer();
    }

    private void setNextPlayer() {
    }

    private void handlePossibleEndCondition() {
        if (checkForWin()) {

        } else if (checkForBust()) {

        }
    }

    private boolean checkForBust() {
        return false;
    }

    private boolean checkForWin() {
        return false;
    }

    public void handleStay() {
    }

    int getCurrentPlayerIndex() {
        return numOfTurns % players.length;
    }

    public static Deck getDeck() {
        return deck;
    }

    public static void setDeck(Deck deck) {
        BlackjackEngine.deck = deck;
    }
}
