package blackjack.presenters;

import blackjack.controllers.BlackjackEngine;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import models.Card;
import models.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class BlackjackGameScene {

    //    public ImageView imageViewQueenOfHearts;
    public AnchorPane anchorPaneRoot;
    public AnchorPane anchorPanePlayerField;
    public HBox hBoxHouseHand;
    public Label lblHouseName;

    public HBox hBoxPlayerOneHand;
    public HBox hBoxPlayerTwoHand;
    public HBox hBoxPlayerThreeHand;
    public HBox hBoxPlayerFourHand;
    public HBox hBoxPlayerFiveHand;

    public Label lblPlayerOneName;
    public Label lblPlayerTwoName;
    public Label lblPlayerThreeName;
    public Label lblPlayerFourName;
    public Label lblPlayerFiveName;
    public ButtonBar btnBarHitOrStay;
    public Button btnHit;
    public Button btnStay;

    Stage stage;
    BlackjackEngine engine;

    List<ObservableList<Card>> playerHands;
    ObservableList<Card> houseHand;
    List<Label> playerNames;


    public void exampleDisplayHand(Player player) {
        AtomicInteger translation = new AtomicInteger(0);
        AtomicInteger rotation = new AtomicInteger(-2 * player.getHand().size());
        if (anchorPaneRoot == null) {
            anchorPaneRoot = new AnchorPane();
        }
        player.getHand().forEach(card -> {
//            card.getImageView().setX(card.getImageView().getX() + translation.get());
//            card.getImageView().setRotate(rotation.doubleValue());
            hBoxPlayerOneHand.getChildren().add(card.getImageView()); // adds card image view to anchor pane
//            translation.getAndAdd(15);
//            rotation.getAndAdd(5);
        });
    }

    public List<ImageView> getImageViews() {
        List<ImageView> imageViews = new ArrayList<>();
        hBoxPlayerOneHand.getChildren().forEach(node -> {
            if (node instanceof ImageView) {
                imageViews.add((ImageView) node);
            }
        });
        return imageViews;
    }

    public void setPlayerNames() {
        Label[] playerLabels = new Label[] {lblPlayerOneName, lblPlayerTwoName, lblPlayerThreeName, lblPlayerFourName, lblPlayerFiveName};
        for (int playerNum = 0; playerNum < getEngine().getPlayers().length; playerNum++) {
            playerLabels[playerNum].setText(getEngine().getPlayers()[playerNum].getName());
        }
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public BlackjackEngine getEngine() {
        return engine;
    }

    public void setEngine(BlackjackEngine engine) {
        this.engine = engine;
    }

    public HBox[] getPlayerHandHBoxs() {
        return new HBox[] {hBoxPlayerOneHand, hBoxPlayerTwoHand, hBoxPlayerThreeHand, hBoxPlayerFourHand, hBoxPlayerFiveHand};
    }

    public void onActionBtnHit(ActionEvent actionEvent) {
        engine.handleHit();
    }

    public void onActionBtnStay(ActionEvent actionEvent) {
        engine.handleStay();
    }

    /**
     * creates listeners to update display every time a player hand is changed
     * only call if blackjack-game-scene.fxml is loaded
     */
    public void setupObservableHandsWithListeners() {
        if (playerHands == null) {
            playerHands = new ArrayList<>();
        }
        Arrays.stream(getEngine().getPlayers()).forEach(player -> playerHands.add(FXCollections.observableArrayList(player.getHand())));
        addListenersToEachPlayerHand();
        houseHand = FXCollections.observableArrayList(getEngine().getHouse().getHand());
        addListenerToHouseHand();
    }

    private void addListenersToEachPlayerHand() {
        playerHands.forEach(hand -> hand.addListener((ListChangeListener<Card>)change ->
                updateHboxOnListChange(change, getPlayerHandHBoxs()[playerHands.indexOf(hand)])));

//            for (int playerNum = 0; playerNum < getEngine().getPlayers().length; playerNum++) {
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

    }

    private void addListenerToHouseHand() {
//        if (fxmlLoader.getController() instanceof BlackjackGameScene) {
                getHouseHand().addListener((ListChangeListener<Card>) change -> {
//                    BlackjackGameScene blackjackGameScene = fxmlLoader.getController();
//                    HBox hBoxHouseHand = blackjackGameScene.hBoxHouseHand;
                     // checks for HBox before casting
                    updateHboxOnListChange(change, getHBoxHouseHand());
//                        hBoxOfCurrentPlayer.getChildren().forEach(node -> System.out.println("Node: " + node));

//                    blackjackGameScene.hBoxPlayerOneHand.getChildren().forEach(System.out::println);
                });
//            rotateCardsInHand(i);
//            }

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

    public HBox getHBoxHouseHand() {
        return hBoxHouseHand;
    }

    public void setHBoxHouseHand(HBox hBoxHouseHand) {
        this.hBoxHouseHand = hBoxHouseHand;
    }

    public void highlightCurrentPlayerName(int currentPlayerIndex) {
        if (playerNames == null) {
            createPlayerNamesList();
        }
        int previousPlayerIndex = currentPlayerIndex - 1 < 0 ? getEngine().getPlayers().length : currentPlayerIndex - 1;

        if (previousPlayerIndex == getEngine().getPlayers().length) {
        lblHouseName.setStyle("-fx-border-color: transparent");
        } else {
        playerNames.get(previousPlayerIndex).setStyle("-fx-border-color: transparent");
        }
        if (currentPlayerIndex == getEngine().getPlayers().length) {
            lblHouseName.setStyle("-fx-border-color: orange");
        } else {
            playerNames.get(currentPlayerIndex).setStyle("-fx-border-color: orange");
        }
    }

    private void createPlayerNamesList() {
        playerNames = new ArrayList<>(Arrays.asList(
                lblPlayerOneName, lblPlayerTwoName, lblPlayerThreeName, lblPlayerFourName, lblPlayerFiveName));
        playerNames.forEach(name -> name.setStyle("-fx-border-color: transparent; -fx-border-width: 3; -fx-border-radius: 50%; -fx-padding: 5"));
        lblHouseName.setStyle("-fx-border-color: transparent; -fx-border-width: 3; -fx-border-radius: 50%; -fx-padding: 5");
    }

    public ObservableList<Card> getHouseHand() {
        return houseHand;
    }

    public void setHouseHand(ObservableList<Card> houseHand) {
        this.houseHand = houseHand;
    }
}
