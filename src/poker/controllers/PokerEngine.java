package poker.controllers;

import javafx.collections.ObservableList;
import javafx.collections.ObservableListBase;
import models.*;
import poker.models.GameStage;
import poker.models.PokerModel;

import java.util.ArrayList;
import java.util.Collections;

public class PokerEngine {
    private int bankInitValue = 100;

    private PokerModel model = new PokerModel();


    public PokerEngine(ArrayList<Player> players) {
        //Get initial data
        for (Player player: players) {
            player.setBank(bankInitValue);
        }


        //set initial data in the model
        model.setPlayerList(players);
    }

    public PokerEngine(PokerModel model) {
        this.model = model;
    }



    public void resetGame() {
        model.resetPlayerHasFolded();
        model.resetDeck();
        model.clearPlayersHands();
        model.setMoneyPool(0);
        model.setRaiseAmount(0);
        model.setCurrentBet(0);
        model.setCurrentPlayerIndex(0);
        model.setLastPlayerToRaise(-1);
        model.setGameStage(GameStage.ANTE);
        model.setInBetPhase(true);

        removePlayersInDebt();
    }

    private void removePlayersInDebt() {
        int limit = -500;
        for (int i = 0; i < model.getPlayerList().size(); i++) {
            if(model.getPlayerList().get(i).getBank() <= limit) {
                model.getPlayerList().remove(i);
                //Since player is getting removed from the list that we are evaluating, stay at same index
                i--;
            }
        }
    }

    public void anteUp() {
        for (Player player: model.getPlayerList()) {
            player.setBank(player.getBank() - 1); // Remove dollar from each player
            model.addToMoneyPool(1);//Add that dollar to money pool
        }
    }

    public void discardCards() {
        int cardsRemoved = 0;
        for(int i = 0; i < model.getCurrentPlayer().getHand().size(); i++) {
            //If player chose to remove card, remove from hand and add one to the count
            if(model.getCardsToDiscard()[i]) {
                cardsRemoved++;
                Card card = model.getCurrentPlayer().getHand().get(i);
                model.getCurrentPlayer().removeFromHand(card);
            }
        }
        //Give player the same amount of cards that they discarded
        givePlayerCards(getModel().getCurrentPlayer(), cardsRemoved);
        model.switchTurn();
        model.resetCardsToDiscard();

        //If the next player is player 1, end discard phase and move to bet phase
        if(model.getCurrentPlayerIndex() == 0) {
            model.setGameStage(GameStage.BET);
        }
    }

    public void putMoneyInPool(int amount) {
        //Remove money from player and put in money pool
        //Players can only bet up to $1000 past their current bank
        if(amount <= (model.getCurrentPlayer().getBank() + 1000)+1) {
            //Remove money from player
            model.getCurrentPlayer().setBank(model.getCurrentPlayer().getBank() - amount);
            //Add money to pool
            model.addToMoneyPool(amount);
        }
    }

    public void placeBet(int betValue) {

        //if not making first bet and its the first players turn, switch to raise phase
        if(model.getCurrentBet() != 0 && model.getCurrentPlayerIndex() == 0) model.setInBetPhase(false);

        //if there is no bet, place bet
        if(model.getCurrentBet() == 0) {
            model.setLastPlayerToRaise(model.getCurrentPlayerIndex());
            model.setCurrentBet(betValue);
        }
        //If there is bet, raise amount
        else {
            raiseBet(betValue);
        }

        if(model.isInBetPhase()) putMoneyInPool(model.getCurrentBet());
        else putMoneyInPool(model.getRaiseAmount());

        //switch turn
        model.switchTurn();
    }

    public void raiseBet(int raiseValue) {

        model.setRaiseAmount(raiseValue);
        model.setLastPlayerToRaise(model.getCurrentPlayerIndex());
        model.raiseCurrentBet(raiseValue);
    }

    public void fold() {
        model.foldPlayerByIndex(getModel().getCurrentPlayerIndex());
        model.switchTurn();
//        checkIfBetPhaseIsOver();
    }
    public void call() {
        //if not making first bet and its the first players turn, switch to raise phase
        if(model.getCurrentBet() != 0 && model.getCurrentPlayerIndex() == 0) model.setInBetPhase(false);

        if(model.isInBetPhase()) putMoneyInPool(model.getCurrentBet());
        else putMoneyInPool(model.getRaiseAmount());


        model.switchTurn();
//        checkIfBetPhaseIsOver();
    }
    public void goAllIn() {
        //1000 is how much extra a player can bet past their balance
        int maxAmount = 1000 + ((int) model.getCurrentPlayer().getBank());
        putMoneyInPool(maxAmount);
        model.switchTurn();

    }

    public boolean checkIfBetPhaseIsOver() {
        //If the current player is the last person to raise (made it once around the table), then move to next phase
        if(model.getCurrentPlayerIndex() == model.getLastPlayerToRaise()) {
            model.setGameStage(GameStage.END);
            return true;
        }
        return false;
    }


    public void distributeCards() {
        for (Player player: model.getPlayerList()) {
            //If bank acc is high enough, give them cards
            if(player.getBank() > -500) {
                //Give 5 cards
                givePlayerCards(player, 5);
            }
        }
    }

    private void givePlayerCards(Player player, int numOfCards) {
        for(int i = 0; i < numOfCards; i++) {
            //Give top card
            player.addToHand(model.getDeck().getTopCard());
            //Remove top card from deck
            model.getDeck().removeTopCard();
        }
    }

    private void giveMoneyPool(Player player) {
        player.setBank(player.getBank() + model.getMoneyPool());
        model.setMoneyPool(0);
    }


    public Player determineWinner() {
        int[][] playerHands = new int[model.getNumberOfActivePlayers()][2];
        for (int i = 0; i < model.getPlayersWhoHaveNotFolded().size(); i++) {
            playerHands[i] = determineHand(model.getPlayersWhoHaveNotFolded().get(i).getHand());
        }
        //default winner to first player
        int winnerIndex = 0;
        //Look at each player after to compare
        for (int i = 1; i < model.getPlayersWhoHaveNotFolded().size(); i++) {
            //If observed player has higher score than current winning player, make observed player the new winning player
            if(playerHands[i][0] > playerHands[winnerIndex][0]) winnerIndex = i;
            //If the type of hands are the same, check the highest card value;
            if(playerHands[i][0] == playerHands[winnerIndex][0]) {
                if(playerHands[i][1] > playerHands[winnerIndex][1]) winnerIndex = i;
            }
            //in any other case, remain the same
        }

        //Get winning player
        Player winner = model.getPlayersWhoHaveNotFolded().get(winnerIndex);

        //Give winner all of the pool money
        giveMoneyPool(winner);

        return winner;
    }

    //return a pain of integer values. First element is the hand ranking(flush) represented as an int (higher is better).
    //Second is the highest value in the hand ranking. This will be useful for tie breakers.
    public int[] determineHand(ObservableList<Card> playerHand) {
        int highestNumInHandRanking = containsRoyalFlush(playerHand);
        if(highestNumInHandRanking > 0) return new int[]{9, highestNumInHandRanking};

        highestNumInHandRanking = containsStraightFlush(playerHand);
        if(highestNumInHandRanking > 0) return new int[]{8, highestNumInHandRanking};

        highestNumInHandRanking = containsFourOfAKind(playerHand);
        if(highestNumInHandRanking > 0) return new int[]{7, highestNumInHandRanking};

        highestNumInHandRanking = containsFullHouse(playerHand);
        if(highestNumInHandRanking > 0) return new int[]{6, highestNumInHandRanking};

        highestNumInHandRanking = containsFlush(playerHand);
        if(highestNumInHandRanking > 0) return new int[]{5, highestNumInHandRanking};

        highestNumInHandRanking = containsStraight(playerHand);
        if(highestNumInHandRanking > 0) return new int[]{4, highestNumInHandRanking};

        highestNumInHandRanking = containsThreeOfAKind(playerHand);
        if(highestNumInHandRanking > 0) return new int[]{3, highestNumInHandRanking};

        highestNumInHandRanking = containsTwoPair(playerHand);
        if(highestNumInHandRanking > 0) return new int[]{2, highestNumInHandRanking};

        highestNumInHandRanking = containsOnePair(playerHand);
        if(highestNumInHandRanking > 0) return new int[]{1, highestNumInHandRanking};

        highestNumInHandRanking = findHighestCard(playerHand);
        return new int[]{0, highestNumInHandRanking};

    }

    //Return 0 if not found, else return highest number in hand ranking
    private int containsRoyalFlush(ObservableList<Card>  playerHand) {
        if(cardsAreOfSameSuit(playerHand) && deckContainsStraight(playerHand) == 2) return findHighestCard(playerHand);
        return 0;
    }

    //Return 0 if not found, else return highest number in hand ranking
    private int containsStraightFlush(ObservableList<Card>  playerHand) {
        if(cardsAreOfSameSuit(playerHand) && deckContainsStraight(playerHand) > 0) return findHighestCard(playerHand);
        return 0;
    }

    //Return 0 if not found, else return highest number in hand ranking
    private int containsFourOfAKind(ObservableList<Card>  playerHand) {
        return findCardsOfSameRank(4, playerHand);
    }

    //Return 0 if not found, else return highest number in hand ranking
    private int containsFullHouse(ObservableList<Card>  playerHand) {
        int threeOfAKindRank = findCardsOfSameRank(3, playerHand);
        if(threeOfAKindRank == 0) return 0; //If first pair is never found, don't search for another
        int secondPairRank = findCardsOfSameRank(2, playerHand, threeOfAKindRank);
        if(secondPairRank == 0) return 0;// If second pair is not found, return 0
        return threeOfAKindRank > secondPairRank ? threeOfAKindRank : secondPairRank;  //Return higher rank of the two
    }

    //Return 0 if not found, else return highest number in hand ranking
    private int containsFlush(ObservableList<Card>  playerHand) {
        if(cardsAreOfSameSuit(playerHand)) return findHighestCard(playerHand);
        return 0;
    }

    //Return 0 if not found, else return highest number in hand ranking
    private int containsStraight(ObservableList<Card>  playerHand) {
        //if deck has straight or special straight
        if(deckContainsStraight(playerHand) > 0) return findHighestCard(playerHand);
        return 0;
    }

    //Return 0 if not found, else return highest number in hand ranking
    private int containsThreeOfAKind(ObservableList<Card>  playerHand) {
        return findCardsOfSameRank(3, playerHand);
    }

    //Return 0 if not found, else return highest number in hand ranking
    private int containsTwoPair(ObservableList<Card>  playerHand) {
        int foundPairRank = findCardsOfSameRank(2, playerHand);
        if(foundPairRank == 0) return 0; //If first pair is never found, don't search for another
        int secondPairRank = findCardsOfSameRank(2, playerHand, foundPairRank);
        if(secondPairRank == 0) return 0;// If second pair is not found, return 0
        return foundPairRank > secondPairRank ? foundPairRank : secondPairRank;  //Return higher rank of the two
    }

    //Return 0 if not found, else return highest number in hand ranking
    private int containsOnePair(ObservableList<Card>  playerHand) {
        return findCardsOfSameRank(2, playerHand);
    }

    //return highest number card in hand
    private int findHighestCard(ObservableList<Card>  playerHand) {
        int highestRank = 0;
        for (Card card: playerHand) {
            if(card.getRank().getValue() > highestRank) highestRank = card.getRank().getValue();
        }
        return highestRank;
    }



    //Overloaded method for when ignoreRank does not matter
    private int findCardsOfSameRank(int numberOfCards, ObservableList<Card>  playerHand) {
        return findCardsOfSameRank(numberOfCards, playerHand, -1);
    }

    //Pass in a value into the ignoreRank to ignore that rank when looking for cards of the same rank
    private int findCardsOfSameRank(int numberOfCards, ObservableList<Card>  playerHand, int ignoreRank) {
        for(int i = 0; i < playerHand.size() -1; i++) {
            int cardRank = playerHand.get(i).getRank().getValue(); //Get cardRank of observed card
            int cardsOfAKind = 1;
            for(int j = i+1; j < playerHand.size(); j++) {
                if(cardRank == playerHand.get(j).getRank().getValue()) cardsOfAKind++;
            }
            //If the method found the right number of cards of a kind, it will return it
            if(cardsOfAKind >= numberOfCards && cardRank != ignoreRank) return cardRank;
        }
        //Return 0 by default. 0 represents that the case was not found
        return 0;
    }

    //Return 0 if deck does not contain straight, 1 if there is a straight, and 2 if straight is (A K Q J 10)
    private int deckContainsStraight(ObservableList<Card>  playerHand) {
        //Make a shallow copy of the cards and sort them
        //ArrayList<Card> cards = (ArrayList<Card>)((ArrayList<Card>) playerHand).clone();
        ObservableList<Card> cards = playerHand;
        Collections.sort(cards, new CardComparator());
        //Loop through cards and determine if they are in a straight (increment by 1)
        int numOfCardsInStraight = 1;
        int prevRankValue = cards.get(0).getRankValue();
        for(int i = 1; i < cards.size(); i++) {
            //If card is one greater than previous card
            if(cards.get(i).getRankValue() == (prevRankValue + 1)) {
                numOfCardsInStraight++;
            }
            prevRankValue = cards.get(i).getRankValue();
        }
        if(numOfCardsInStraight == 5) return 1;
        //Check for special case (A K Q J 10)
        //if 4 in a row, first card is ace and last card is king
        else if(numOfCardsInStraight == 4 && cards.get(0).getRankValue() == 1 && cards.get(4).getRankValue() == 13) {
            return 2;
        }
        return 0;
    }

    //Returns whether all of the cards in a deck are of the same suit
    private boolean cardsAreOfSameSuit(ObservableList<Card>  playerHand) {
        ESuit suit = playerHand.get(0).getSuit();
        for(int i = 1; i < playerHand.size(); i++) {
            if(playerHand.get(i).getSuit() != suit) return false;
        }
        return true;
    }

    public PokerModel getModel() {
        return model;
    }
}
