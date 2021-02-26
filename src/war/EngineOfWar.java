package war;

import models.Card;
import models.Deck;
import models.Player;

import java.util.ArrayList;

public class EngineOfWar {
    //Holds players, if they are passed in (TBD)
    private static Player[] players;
    private static int round;

    //Takes in any number of players and sets up each player's deck
    public static void start(Player... p){
        round = 1;
        players = p;
        Deck deck = new Deck(0);
        for(int i = 0; i < deck.getCards().size(); i++){
            //Splits the deck between players
            try {
                players[i % p.length].addToHand(deck.getCardAt(0));
                deck.removeCard(0);
            } catch (IndexOutOfBoundsException e){
                System.out.println("War Loop incorrect");
            }
        }
    }
    /*
    * TODO change functionality to return cards played for displaying to the user
     */
    public static void takeTurn(){
        Card[] table = new Card[players.length];
        //takes the top card of each player's hand and puts it in play
        for(int i = 0; i < players.length; i++){
            table[i] = players[i].getHand().get(0);
            players[i].getHand().remove(0);
        }
        int[] hold = new int[]{-1, 0};
        boolean isEqual = false;
        for(int i = 0; i < table.length; i++) {
            //checks if larger and held isn't an ace
            if (table[i].getRankValue() > hold[1] && hold[1] != 1) {
                hold[0] = i;
                hold[1] = table[i].getRankValue();
                isEqual = false;
            } else if (table[i].getRankValue() == hold[1]) {
                isEqual = true;
            } else if (table[i].getRankValue() == 1) {
                hold[0] = i;
                hold[1] = 1;
                isEqual = false;
            }
        }
        if(isEqual){
            ArrayList<Integer> tied = new ArrayList<>(); //holds position of players tied for win
            for(int i = 0; i < table.length; i++){
                if(table[i].getRankValue() == hold[1]){
                    tied.add(i);
                }
            }
            goToWar(tied.stream().mapToInt(i -> i).toArray());
        } else {
            players[hold[0]].addToScore(1);
        }

    }
    public static int goToWar(int[] paw){
        //paw stands for "players at war"
        Card[][] table = new Card[paw.length][4];

        //takes cards from each paw and puts them on the table
        for(int i = 0; i < paw.length; i++){
            for(int j = 0; j < 4; j++){
                table[i][j] = players[i].getHand().get(0);
                players[i].getHand().remove(0);
            }
        }

        //checks each player's final card for a win or another war
        int[] hold = new int[]{-1, 0};
        boolean isEqual = false;
        for(int i = 0; i < table.length; i++) {
            //checks if larger and held isn't an ace
            if (table[i][3].getRankValue() > hold[1] && hold[1] != 1) {
                hold[0] = i;
                hold[1] = table[i][3].getRankValue();
                isEqual = false;
            } else if (table[i][3].getRankValue() == hold[1]) {
                isEqual = true;
            } else if (table[i][3].getRankValue() == 1) {
                hold[0] = i;
                hold[1] = 1;
                isEqual = false;
            }
        }
        if(isEqual){
            ArrayList<Integer> tied = new ArrayList<>(); //holds position of players tied for win
            for(int i = 0; i < table.length; i++){
                if(table[i][3].getRankValue() == hold[1]){
                    tied.add(i);
                }
            }
            int winner = goToWar(tied.stream().mapToInt(i -> i).toArray());
            players[winner].addToScore(3);
            return winner;
        } else {
            players[hold[0]].addToScore(3);
            return hold[0];
        }
    }
}
