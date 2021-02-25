package poker.models;

import models.Deck;
import models.Player;

import java.util.ArrayList;

public class PokerModel {
    private ArrayList<Player> playerList = new ArrayList<>();
    private ArrayList<Player> playersWhoHaveNotFolded = new ArrayList<>();
    private Deck deck = new Deck(0); //I have no clue why the deck class doesn't have a default constructor...
    private int moneyPool = 0;
    private int currentPlayerIndex = 0;
    private int currentBet;

}
