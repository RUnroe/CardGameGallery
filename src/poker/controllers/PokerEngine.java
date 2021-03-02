package poker.controllers;

import models.*;
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
        model.resetPlayersWhoHaveNotFolded();
    }

    public PokerEngine(PokerModel model) {
        this.model = model;
    }





    public void anteUp() {
        for (Player player: model.getPlayerList()) {
            player.setBank(player.getBank() - 1); // Remove dollar from each player
            model.addToMoneyPool(1);//Add that dollar to money pool
        }
    }



    //return a pain of integer values. First element is the hand ranking(flush) represented as an int (higher is better).
    //Second is the highest value in the hand ranking. This will be useful for tie breakers.
    public int[] determineHand(Deck playerHand) {
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
    private int containsRoyalFlush(Deck playerHand) {
        if(cardsAreOfSameSuit(playerHand) && deckContainsStraight(playerHand) == 2) return findHighestCard(playerHand);
        return 0;
    }

    //Return 0 if not found, else return highest number in hand ranking
    private int containsStraightFlush(Deck playerHand) {
        if(cardsAreOfSameSuit(playerHand) && deckContainsStraight(playerHand) > 0) return findHighestCard(playerHand);
        return 0;
    }

    //Return 0 if not found, else return highest number in hand ranking
    private int containsFourOfAKind(Deck playerHand) {
        return findCardsOfSameRank(4, playerHand);
    }

    //Return 0 if not found, else return highest number in hand ranking
    private int containsFullHouse(Deck playerHand) {
        int threeOfAKindRank = findCardsOfSameRank(3, playerHand);
        if(threeOfAKindRank == 0) return 0; //If first pair is never found, don't search for another
        int secondPairRank = findCardsOfSameRank(2, playerHand, threeOfAKindRank);
        if(secondPairRank == 0) return 0;// If second pair is not found, return 0
        return threeOfAKindRank > secondPairRank ? threeOfAKindRank : secondPairRank;  //Return higher rank of the two
    }

    //Return 0 if not found, else return highest number in hand ranking
    private int containsFlush(Deck playerHand) {
        if(cardsAreOfSameSuit(playerHand)) return findHighestCard(playerHand);
        return 0;
    }

    //Return 0 if not found, else return highest number in hand ranking
    private int containsStraight(Deck playerHand) {
        //if deck has straight or special straight
        if(deckContainsStraight(playerHand) > 0) return findHighestCard(playerHand);
        return 0;
    }

    //Return 0 if not found, else return highest number in hand ranking
    private int containsThreeOfAKind(Deck playerHand) {
        return findCardsOfSameRank(3, playerHand);
    }

    //Return 0 if not found, else return highest number in hand ranking
    private int containsTwoPair(Deck playerHand) {
        int foundPairRank = findCardsOfSameRank(2, playerHand);
        if(foundPairRank == 0) return 0; //If first pair is never found, don't search for another
        int secondPairRank = findCardsOfSameRank(2, playerHand, foundPairRank);
        if(secondPairRank == 0) return 0;// If second pair is not found, return 0
        return foundPairRank > secondPairRank ? foundPairRank : secondPairRank;  //Return higher rank of the two
    }

    //Return 0 if not found, else return highest number in hand ranking
    private int containsOnePair(Deck playerHand) {
        return findCardsOfSameRank(2, playerHand);
    }

    //return highest number card in hand
    private int findHighestCard(Deck playerHand) {
        int highestRank = 0;
        for (Card card: playerHand.getCards()) {
            if(card.getRank().getValue() > highestRank) highestRank = card.getRank().getValue();
        }
        return highestRank;
    }



    //Overloaded method for when ignoreRank does not matter
    private int findCardsOfSameRank(int numberOfCards, Deck playerHand) {
        return findCardsOfSameRank(numberOfCards, playerHand, -1);
    }

    //Pass in a value into the ignoreRank to ignore that rank when looking for cards of the same rank
    private int findCardsOfSameRank(int numberOfCards, Deck playerHand, int ignoreRank) {
        for(int i = 0; i < playerHand.getCards().size() -1; i++) {
            int cardRank = playerHand.getCardAt(i).getRank().getValue(); //Get cardRank of observed card
            int cardsOfAKind = 1;
            for(int j = i+1; j < playerHand.getCards().size(); j++) {
                if(cardRank == playerHand.getCardAt(j).getRank().getValue()) cardsOfAKind++;
            }
            //If the method found the right number of cards of a kind, it will return it
            if(cardsOfAKind >= numberOfCards && cardRank != ignoreRank) return cardRank;
        }
        //Return 0 by default. 0 represents that the case was not found
        return 0;
    }

    //Return 0 if deck does not contain straight, 1 if there is a straight, and 2 if straight is (A K Q J 10)
    private int deckContainsStraight(Deck playerHand) {
        //Make a shallow copy of the cards and sort them
        ArrayList<Card> cards = (ArrayList<Card>) playerHand.getCards().clone();
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
    private boolean cardsAreOfSameSuit(Deck playerHand) {
        ESuit suit = playerHand.getCardAt(0).getSuit();
        for(int i = 1; i < playerHand.getCards().size(); i++) {
            if(playerHand.getCardAt(i).getSuit() != suit) return false;
        }
        return true;
    }

    public PokerModel getModel() {
        return model;
    }
}
