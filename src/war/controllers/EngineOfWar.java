package war.controllers;

import models.Card;
import models.Deck;
import models.Player;
import war.models.WarModel;

import java.util.ArrayList;

public class EngineOfWar {



    private WarModel model;

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
        Deck deck = new Deck(1);
        for (int i = 0; i < deck.getCards().size(); i++) {
            //Splits the deck between players
            try {
                model.getPlayers()[i % p.length].addToHand(deck.getCardAt(0));
                deck.removeCard(0);
            } catch (IndexOutOfBoundsException e) {
                System.out.println("War Loop incorrect");
            }
        }
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

    public int checkForRoundWin() {
        if(model.getTable()[0].getRankValue() > model.getTable()[1].getRankValue()) {
            return 1;
        }
        else if (model.getTable()[0].getRankValue() < model.getTable()[1].getRankValue()) {
            return 2;
        }
        else {
            return 0;
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

    public Card[][] goToWar(int[] paw) {
        //paw stands for "players at war"
        Card[][] warTable = new Card[paw.length][4];

        //takes cards from each paw and puts them on the table
        for (int i = 0; i < paw.length; i++) {
            for (int j = 0; j < 4; j++) {
                warTable[i][j] = model.getPlayers()[i].getHand().get(0);
                model.getPlayers()[i].getHand().remove(0);
            }
        }
        return warTable;
    }

    public int[] checkWarWinner(Card[][] warTable) {
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
            for(int i = 0; i < model.getTable().length; i++){
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

    public WarModel getModel() {
        return model;
    }
}
