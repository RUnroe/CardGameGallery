package war.models;

import models.Card;
import models.Player;

public class WarModel {

    private Player[] players;
    private int round;
    private Card[] table;

    public Player[] getPlayers() {
        return players;
    }

    public void setPlayers(Player[] players) {
        this.players = players;
    }

    public int getRound() {
        return round;
    }

    public void setRound(int round) {
        this.round = round;
    }

    public Card[] getTable() {
        return table;
    }

    public void setupTable() {
        this.table = new Card[players.length];
    }
}
