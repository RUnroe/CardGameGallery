package war;

import models.Deck;
import models.Player;

public class EngineOfWar {
    //Holds players, if they are passed in (TBD)
    private static Player[] players;

    //Takes in any number of players and sets up each player's deck
    public static void start(Player... p){
        players = p;
        Deck deck = new Deck(0);
        for(int i = 0; i < deck.getCards().size(); i++){
            try{
                players[i % p.length].addToHand(deck.getCardAt(0));
                deck.removeCard(0);
                } catch (IndexOutOfBoundsException e){
                    System.out.println("War Loop incorrect");
                }
        }
    }

}
