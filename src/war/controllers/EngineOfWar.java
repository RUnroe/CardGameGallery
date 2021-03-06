package war.controllers;

import models.Card;
import models.Deck;
import models.Player;
import war.models.WarModel;

import java.util.ArrayList;
import java.util.Collections;

public class EngineOfWar {

    private final WarModel model;

    //Holds cards from war in the case that a second war takes place
    private final ArrayList<Card> warPile = new ArrayList<>();

    public EngineOfWar() {
        this.model = new WarModel();
    }
    public EngineOfWar(WarModel model) {
        this.model = model;
    }



    //Takes in any number of players and sets up each player's deck
    public void start(Player... p) {
        model.setRound(0);
        model.setPlayers(p);
        model.setupTable();
        Deck deck = flipDeck(new Deck(1));
        System.out.println(deck.getCards().size());
//        int deckSize = ((ArrayList<Card>) deck.getCards().clone()).size();
        int deckSize = ((ArrayList<?>) deck.getCards().clone()).size();
        for (int i = 0; i < deckSize; i++) {
            //Splits the deck between players
            try {
                model.getPlayers()[i % p.length].addToHand(deck.getCardAt(0));
                deck.removeCard(0);
            } catch (IndexOutOfBoundsException e) {
                System.out.println("War Loop incorrect");
            }
        }
    }

    private Deck flipDeck(Deck deck) {
        for (Card card: deck.getCards()) {
            card.setFlipped(true);
        }
        return deck;
    }

    public Card getNextCard() {
        int index = model.getRound() % model.getPlayers().length;
        Card[] table = model.getTable();
        Player player = model.getPlayers()[index];
        table[index] = player.getHand().get(0);
        player.getHand().remove(0);
        model.setRound(model.getRound()+1);
        return table[index];
    }

    public int determineRoundWinner() {
        //Make aces high
        int player1CardValue = model.getTable()[0].getRankValue() == 1 ? 14 : model.getTable()[0].getRankValue();
        int player2CardValue = model.getTable()[1].getRankValue() == 1 ? 14 : model.getTable()[1].getRankValue();
        int winner = -1;
        if(player1CardValue > player2CardValue) {
            winner = 0;
        }
        else if (player1CardValue < player2CardValue) {
            winner = 1;
        }
        //If round is not a tie
        if(winner > -1) {
           givePlayerTableCards(winner);
        }
        return winner;
    }
    private void givePlayerTableCards(int playerIndex) {
        for(Card card : model.getTable()){
            model.getPlayers()[playerIndex].addToHand(card);
        }
    }

    public int[] getTied() {
        int[] hold = new int[]{-1, 0};
        ArrayList<Integer> tied = new ArrayList<>(); //holds position of players tied for win
        for (int i = 0; i < model.getTable().length; i++) {
            if (model.getTable()[i].getRankValue() == hold[1]) {
                tied.add(i);
            }
        }
        return tied.stream().mapToInt(i -> i).toArray();
    }

    public Card[][] goToWar(int[] paw, boolean isDoubleWar) {
        //paw stands for "players at war"
        Card[][] warTable = new Card[paw.length][5];

        //takes cards from each paw and puts them on the table
        for (int i = 0; i < paw.length; i++) {
            //put table cards in the warTable
            //If is not double war, put first card at start of list
            if(!isDoubleWar){
                warTable[i][0] = model.getTable()[i];
                for (int j = 1; j < 5; j++) {
                    warTable[i][j] = model.getPlayers()[i].getHand().get(0);
                    model.getPlayers()[i].getHand().remove(0);
                }
            }
            else {
                for (int j = 0; j < 4; j++) {
                    warTable[i][j] = model.getPlayers()[i].getHand().get(0);
                    model.getPlayers()[i].getHand().remove(0);
                }
            }
        }
        return warTable;
    }

    public int checkWarWinner(Card[][] warTable) {
        //Make aces high
        int player1CardValue = warTable[0][warTable[0].length-1].getRankValue() == 1 ? 14 : warTable[0][warTable[0].length-1].getRankValue();
        int player2CardValue = warTable[1][warTable[1].length-1].getRankValue() == 1 ? 14 : warTable[1][warTable[1].length-1].getRankValue();
        int winner = -1;
        if(player1CardValue > player2CardValue) {
            winner = 0;
        }
        else if (player1CardValue < player2CardValue) {
            winner = 1;
        }
        //Store cards from war in a list
        storeWarCards(warTable);
        //If war is not a tie
        if(winner > -1) {
            givePlayerWarCards(winner);
            //Clear war pile
            warPile.clear();
        }
        return winner;
    }
    private void givePlayerWarCards( int playerIndex) {
        for (Card card: warPile) {
            model.getPlayers()[playerIndex].addToHand(card);
        }
    }

    private void storeWarCards(Card[][] warTable) {
        for (Card[] hand: warTable) {
            Collections.addAll(warPile, hand);
        }
    }

    //return index of winning player. Return -1 for no win
    public int checkForWin() {
        if(model.getPlayers()[0].getHand().size() < 4) return 1;
        else if(model.getPlayers()[1].getHand().size() < 4) return 0;
        return -1;
    }

    public WarModel getModel() {
        return model;
    }
}
