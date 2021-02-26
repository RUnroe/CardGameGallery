package war;

import models.Card;
import models.Deck;
import models.Player;

import java.util.ArrayList;

public class EngineOfWar {
    //Holds players, if they are passed in (TBD)
    private static Player[] players;
    private static int round;
    private static Card[] table;


    //Takes in any number of players and sets up each player's deck
    public static void start(Player... p) {
        round = 1;
        players = p;
        table = new Card[players.length];
        Deck deck = new Deck(0);
        for (int i = 0; i < deck.getCards().size(); i++) {
            //Splits the deck between players
            try {
                players[i % p.length].addToHand(deck.getCardAt(0));
                deck.removeCard(0);
            } catch (IndexOutOfBoundsException e) {
                System.out.println("War Loop incorrect");
            }
        }
    }

    public static Card getNextCard() {
        table[round % players.length] = players[round % players.length].getHand().get(0);
        players[round % players.length].getHand().remove(0);
        return table[round % players.length];
    }

    public static boolean checkForHighest() {
        int[] hold = new int[]{-1, 0};
        boolean isEqual = false;
        for (int i = 0; i < table.length; i++) {
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
        return isEqual;
    }

    public static int[] getTied() {
        int[] hold = new int[]{-1, 0};
        ArrayList<Integer> tied = new ArrayList<>(); //holds position of players tied for win
        for (int i = 0; i < table.length; i++) {
            if (table[i].getRankValue() == hold[1]) {
                tied.add(i);
            }
        }
        return tied.stream().mapToInt(i -> i).toArray();
    }

    public static Card[][] goToWar(int[] paw) {
        //paw stands for "players at war"
        Card[][] warTable = new Card[paw.length][4];

        //takes cards from each paw and puts them on the table
        for (int i = 0; i < paw.length; i++) {
            for (int j = 0; j < 4; j++) {
                warTable[i][j] = players[i].getHand().get(0);
                players[i].getHand().remove(0);
            }
        }
        return warTable;
    }

    public static int[] checkWarWinner(Card[][] warTable) {
        //checks each player's final card for a win or another war
        int[] hold = new int[]{-1, 0};
        boolean isEqual = false;
        for (int i = 0; i < warTable.length; i++) {
            //checks if larger and held isn't an ace
            if (warTable[i][3].getRankValue() > hold[1] && hold[1] != 1) {
                hold[0] = i;
                hold[1] = warTable[i][3].getRankValue();
                isEqual = false;
            } else if (warTable[i][3].getRankValue() == hold[1]) {
                isEqual = true;
            } else if (warTable[i][3].getRankValue() == 1) {
                hold[0] = i;
                hold[1] = 1;
                isEqual = false;
            }
        }
        if (isEqual) {
            ArrayList<Integer> tied = new ArrayList<>(); //holds position of players tied for win
            for(int i = 0; i < table.length; i++){
                if(warTable[i][3].getRankValue() == hold[1]){
                    tied.add(i);
                }
            }
        return tied.stream().mapToInt(i -> i).toArray();
        } else {
            return new int[]{hold[0]};
//        }
        }
    }
}
