package poker.controllers;

import models.Card;
import models.Deck;
import models.Player;
import poker.models.PokerModel;

import java.util.ArrayList;

public class PokerEngine {
    private int bankInitValue = 100;
    private PokerModel model = new PokerModel();


    public PokerEngine(String[] playerNames, boolean playingAgainstAI) {
        //Get initial data
        ArrayList<Player> playerList = new ArrayList<>();
        for(int i = 0; i < playerNames.length; i++) {
            Player player = new Player(playerNames[i], bankInitValue);
            playerList.add(player);
        }
        int numOfPlayers = playerList.size();
        if(playingAgainstAI) playerList.get(numOfPlayers-1).setPlayerAI(true);

        //set initial data in the model
        model.setPlayerList(playerList);
        model.resetPlayersWhoHaveNotFolded();
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
        return 0;
    }

    //Return 0 if not found, else return highest number in hand ranking
    private int containsStraightFlush(Deck playerHand) {
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
        //Same suit
        return 0;
    }

    //Return 0 if not found, else return highest number in hand ranking
    private int containsStraight(Deck playerHand) {
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
            if(card.getRank() > highestRank) highestRank = card.getRank();
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
            int cardRank = playerHand.getCardAt(i).getRank(); //Get cardRank of observed card
            int cardsOfAKind = 1;
            for(int j = i+1; j < playerHand.getCards().size(); j++) {
                if(cardRank == playerHand.getCardAt(j).getRank()) cardsOfAKind++;
            }
            //If the method found the right number of cards of a kind, it will return it
            if(cardsOfAKind >= numberOfCards && cardRank != ignoreRank) return cardRank;
        }
        //Return 0 by default. 0 represents that the case was not found
        return 0;
    }
}
