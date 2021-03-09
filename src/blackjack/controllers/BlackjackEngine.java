package blackjack.controllers;

//region Imports
import blackjack.presenters.BlackjackGameScene;
import blackjack.presenters.BlackjackHomeScene;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import models.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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

    double priceOfHand;

    //region Setup Methods
    public void setupHomeGui() {
        changeScene(HOME_PATH);
        if (fxmlLoader.getController() instanceof BlackjackHomeScene) {
            BlackjackHomeScene blackjackHomeScene = fxmlLoader.getController();
            blackjackHomeScene.setStage(getStage());
            blackjackHomeScene.setEngine(this);
            getStage().setX(0);
            getStage().setY(0);
        }
    }
    public void setupGameGui(boolean isNewGame) {
        getStage().setX(0);
        getStage().setY(0);
        changeScene(GAME_PATH);
        if (fxmlLoader.getController() instanceof BlackjackGameScene) {
            BlackjackGameScene blackjackGameScene = fxmlLoader.getController();
            blackjackGameScene.setStage(getStage());
            blackjackGameScene.setEngine(this);
            blackjackGameScene.setPlayerNames();
            blackjackGameScene.setupObservableHandsWithListeners();
//            blackjackGameScene.rdBtnOneDollar.setToggleGroup(blackjackGameScene.tglGrpPriceOfHand);
//            blackjackGameScene.rdBtnFiveDollars.setToggleGroup(blackjackGameScene.tglGrpPriceOfHand);
//            blackjackGameScene.rdBtnTenDollars.setToggleGroup(blackjackGameScene.tglGrpPriceOfHand);
            showStartRoundVBox();
//            setupEngine();
//            blackjackGameScene.highlightCurrentPlayerName(getCurrentPlayerIndex());
        }
    }

    public void setupEngine() {
//        addListenersToEachPlayerHand();
//        addListenerToHouseHand();
        numOfTurns = -1;
        if (getDeck() == null || getDeck().getCards().size() < 60) {
            setDeck(new Deck(6));
        }
        setupStartingHands();
        passTurn();
//        for (int i = 0; i < numOfPlayers; i++) {
//            if (players[i] == null) {
//                players[i] = new Player("Player " + i, 20.00);
//            }
//        }
//        player = new Player("Don");
    }

    public void setup(Stage stage) {
//        setupEngine();
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
//    private void addListenersToEachPlayerHand() {
//        if (fxmlLoader.getController() instanceof BlackjackGameScene) {
//            BlackjackGameScene blackjackGameScene = fxmlLoader.getController();
//            for (int playerNum = 0; playerNum < players.length; playerNum++) {
//                HBox hBoxOfCurrentPlayer = blackjackGameScene.getPlayerHandHBoxs()[playerNum];
//                players[playerNum].getHand().addListener((ListChangeListener<Card>) change -> {
////                    BlackjackGameScene blackjackGameScene = fxmlLoader.getController();
////                    ObservableList<Node> playerFieldChildren = blackjackGameScene.anchorPanePlayerField.getChildren();
////                    if (playerFieldChildren.get(finalI) instanceof HBox) { // checks for HBox before casting
////                        HBox hBoxOfCurrentPlayer = (HBox) playerFieldChildren.get(finalI);
//                        updateHboxOnListChange(change, hBoxOfCurrentPlayer);
////                        hBoxOfCurrentPlayer.getChildren().forEach(node -> System.out.println("Node: " + node));
////                    }
////                    blackjackGameScene.hBoxPlayerOneHand.getChildren().forEach(System.out::println);
//                });
////            rotateCardsInHand(i);
//            }
//        }
//    }

//    private void addListenerToHouseHand() {
//        if (fxmlLoader.getController() instanceof BlackjackGameScene) {
//                house.getHand().addListener((ListChangeListener<Card>) change -> {
//                    BlackjackGameScene blackjackGameScene = fxmlLoader.getController();
//                    HBox hBoxHouseHand = blackjackGameScene.hBoxHouseHand;
//                     // checks for HBox before casting
//                    updateHboxOnListChange(change, hBoxHouseHand);
////                        hBoxOfCurrentPlayer.getChildren().forEach(node -> System.out.println("Node: " + node));
//
////                    blackjackGameScene.hBoxPlayerOneHand.getChildren().forEach(System.out::println);
//                });
////            rotateCardsInHand(i);
//            }
//
//    }

//    private void updateHboxOnListChange(ListChangeListener.Change<? extends Card> change, HBox hBox) {
//        while (change.next()) { // checks for change
//            if (change.wasAdded()) { // checks for additions to the hand
//                change.getAddedSubList().forEach(card -> {
//                    hBox.getChildren().add(card.getImageView()); // adds cards to display
//                });
//            } else if (change.wasRemoved()) { // checks for removal from hand
//                change.getAddedSubList().forEach(card -> {
//                    hBox.getChildren().remove(card.getImageView()); // removes cards from display
//                });
//            } else if (change.wasPermutated()) {
//                hBox.getChildren().clear();
//                change.getList().forEach(card -> {
//                    hBox.getChildren().add(card.getImageView()); // adds cards to display
//                });
//            }
//        }
//    }

    private void setupStartingHands() {
        Arrays.stream(players).forEach(player -> {
            if (player.getHand() == null) {
                player.setHand(new ArrayList<>());
            }
            clearAndUpdateObservableHands(player);
            if (player.getBank() > -50.00) {
            buyInToRound(player);
            }
        });
        if (getHouse().getHand() == null) {
            house.setHand(new ArrayList<>());
        }
        clearAndUpdateObservableHands(house);
        for (int numOfInitialCards = 0; numOfInitialCards < 2; numOfInitialCards++) {
            drawOnceForAllHands();
        }
        sortAllHands();
        flipAllCardsExceptHoleCard();
    }

    private void buyInToRound(Player player) {
        player.setBank(player.getBank() - getPriceOfHand());
    }

    private void drawOnceForAllHands() {
        Arrays.stream(players).forEach(player -> {
            if (player.getBank() > -50) {
            drawAndUpdateObservableHand(player);
            }
        });
        drawAndUpdateObservableHand(house);
    }

    private Card drawAndUpdateObservableHand(Player player) {
        Card card = player.drawFromDeck(getDeck());
        if (fxmlLoader.getController() instanceof BlackjackGameScene) {
            BlackjackGameScene blackjackGameScene = fxmlLoader.getController();
            if (!player.equals(getHouse())) {
                // gets the hbox from a list at the same index as the player in the players list,
                // and adds the new card's image view as a child
//                blackjackGameScene.getPlayerHandHBoxs()[Arrays.stream(players).collect(Collectors.toList())
//                        .indexOf(player)].getChildren().add(card.getImageView());
                blackjackGameScene.getPlayerHands().get(Arrays.stream(players).collect(Collectors.toList())
                        .indexOf(player)).add(card);
            } else {
//                blackjackGameScene.getHBoxHouseHand().getChildren().add(card.getImageView());
                blackjackGameScene.getHouseHand().add(card);
            }
        }
        return card;
    }

    private void clearAndUpdateObservableHands(Player player) {
        if (fxmlLoader.getController() instanceof BlackjackGameScene) {
            BlackjackGameScene blackjackGameScene = fxmlLoader.getController();
            player.getHand().clear();
            if (!player.equals(getHouse())) {
                // gets the hbox from a list at the same index as the player in the players list,
                // and clears the hbox of children
                blackjackGameScene.getPlayerHandHBoxs()[Arrays.stream(players).collect(Collectors.toList())
                        .indexOf(player)].getChildren().clear();
//                for (int playerNum = 0; playerNum < players.length; playerNum++) {
//                    for (Card card : new ArrayList<>(blackjackGameScene.getPlayerHands().get(playerNum))) {
//                        blackjackGameScene.getPlayerHands().get(playerNum).remove(card);
//                    }
//                }
                blackjackGameScene.getPlayerHands().get(Arrays.stream(players).collect(Collectors.toList())
                        .indexOf(player)).clear();
            } else {
                blackjackGameScene.getHBoxHouseHand().getChildren().clear();
//                for (Card card : new ArrayList<>(blackjackGameScene.getHouseHand())) {
//                    blackjackGameScene.getHouseHand().remove(card);
//                }
                blackjackGameScene.getHouseHand().clear();
            }
        }
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
//        flipCardAndUpdateDisplay(house, 0);
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
        int index = getCurrentPlayerIndex();
        Player currentPlayer = index < players.length ? players[index] : house;
        Card newCard = drawAndUpdateObservableHand(currentPlayer);
        flipCardAndUpdateDisplay(currentPlayer, newCard);
        if(hasEndTurnCondition(currentPlayer)) {
            passTurn();
        }
    }

    private boolean hasEndTurnCondition(Player currentPlayer) {
        System.out.println("Total core for " + currentPlayer.getName() + ": " + calculateHandTotal(currentPlayer));
        return calculateHandTotal(currentPlayer) >= 21 || currentPlayer.getBank() <= -50.00 || currentPlayer.getHand().size() > 4;
    }




    private boolean hasLostOnTurn(Player player) {
        return calculateHandTotal(player) > 21 || player.getBank() <= -50.00;
    }

    private int calculateHandTotal(Player player) {
        //TODO fix face card values to be 10 points for blackjack
        int totalValue = 0;//player.getHandTotal();
        for (Card card : player.getHand()) {
            int cardValue = switch (card.getRank()) {
                case JACK, QUEEN, KING -> 10;
                default -> card.getRankValue();
            };
            totalValue += cardValue;
        }
        int numOfAces = (int) player.getHand().stream().filter(card -> card.getRank().equals(ERank.ACE)).count();
        System.out.println("Aces# " + numOfAces);
        while (totalValue < 12 && numOfAces > 0) {
            totalValue += 10;
            numOfAces--;
        }
        return totalValue;
    }

    private void passTurn() {
        if (fxmlLoader.getController() instanceof BlackjackGameScene) {
            BlackjackGameScene blackjackGameScene = fxmlLoader.getController();
            numOfTurns++;
            blackjackGameScene.highlightCurrentPlayerName(getCurrentPlayerIndex());
            if (getCurrentPlayerIndex() >= players.length) {
                if (!hasEndTurnCondition(house)) {
                    takeHouseTurn();
                }
                handleEndOfRound();
            } else if (hasEndTurnCondition(players[getCurrentPlayerIndex()])) {
                passTurn();
            }
        }
    }

    private void handleEndOfRound() {
        calculatePayouts();
        showStartRoundVBox();
    }

    private void showStartRoundVBox() {
        if (fxmlLoader.getController() instanceof BlackjackGameScene) {
            BlackjackGameScene blackjackGameScene = fxmlLoader.getController();
            blackjackGameScene.updateBanks();
            blackjackGameScene.btnBarHitOrStay.setVisible(false);
            blackjackGameScene.btnBarHitOrStay.setDisable(true);
            blackjackGameScene.vBoxStartRound.setVisible(true);
            blackjackGameScene.vBoxStartRound.setDisable(false);
        }
    }

    public void setupNewRound(double priceOfHand) {
        if (fxmlLoader.getController() instanceof BlackjackGameScene) {
            BlackjackGameScene blackjackGameScene = fxmlLoader.getController();
            blackjackGameScene.vBoxStartRound.setVisible(false);
            blackjackGameScene.vBoxStartRound.setDisable(true);
            blackjackGameScene.btnBarHitOrStay.setVisible(true);
            blackjackGameScene.btnBarHitOrStay.setDisable(false);
//            blackjackGameScene.radio
            setPriceOfHand(priceOfHand);
            setupEngine();
            blackjackGameScene.updateBanks();
            blackjackGameScene.highlightCurrentPlayerName(getCurrentPlayerIndex());
        }

    }

    private void calculatePayouts() {
        int houseTotal = calculateHandTotal(house);
        Arrays.stream(players).forEach(player -> {
            if (player.getBank() > -50) {
                int playerTotal = calculateHandTotal(player);
                if (player.getHand().size() > 4) { // 5-card charlie 4x // this win is independent of hand totals
                    payPlayer(player, 4);
                } else if (playerTotal == houseTotal) { // draw 1x // no matter hand total player == house is draw
                    payPlayer(player, 1);
                } else if (playerTotal == 21) { // blackjack 3x // getting here requires player hand total != house so not a draw
                    payPlayer(player, 3);
                } else if (playerTotal < 21 && playerTotal > houseTotal) { // win 2x // player hand total must be higher than house without busting
                    payPlayer(player, 2);
                }
            }
        });
    }

    private void payPlayer(Player player, int payoutScalar) {
        player.setBank(player.getBank() + (payoutScalar * getPriceOfHand()));
    }

    private void takeHouseTurn() {
        if (fxmlLoader.getController() instanceof BlackjackGameScene) {
            BlackjackGameScene blackjackGameScene = fxmlLoader.getController();
            blackjackGameScene.btnHit.setDisable(true);
            blackjackGameScene.btnStay.setDisable(true);
            house.getHand().forEach(card -> {if(!card.isFlipped()) {flipCardAndUpdateDisplay(house, card);}});
            while (calculateHandTotal(house) < 17) {
                handleHit();
            }
            blackjackGameScene.btnHit.setDisable(false);
            blackjackGameScene.btnStay.setDisable(false);
//            handleStay();
        }
    }

    private void setNextPlayer() {
        numOfTurns++;
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
        passTurn();
    }

    int getCurrentPlayerIndex() {
        System.out.println("Player turn: " + (numOfTurns % (players.length + 1)));
        return numOfTurns % (players.length + 1);
    }

    public static Deck getDeck() {
        return deck;
    }

    public static void setDeck(Deck deck) {
        BlackjackEngine.deck = deck;
    }

    public Player getHouse() {
        return house;
    }

    public void setHouse(Player house) {
        this.house = house;
    }

    public double getPriceOfHand() {
        return priceOfHand;
    }

    public void setPriceOfHand(double priceOfHand) {
        this.priceOfHand = priceOfHand;
    }
}
