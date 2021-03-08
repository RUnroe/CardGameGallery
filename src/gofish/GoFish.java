package gofish;

import models.Card;
import models.Deck;
import models.ERank;
import models.Player;

import java.util.ArrayList;

public class GoFish {
    private static Player[] players;
    private static int[] scores;
    private static Deck deck;
    private static int turn;

    public GoFish(Player... p){
        turn = 0;
        players = p;
        scores = new int[players.length];
        deck = new Deck(1);
        int l = players.length < 4 ? 7 : 5;
        for(int i = 0; i < l; i++){
            for(int j = 0; j < players.length; j++){
                players[j].addToHand(deck.getTopCard());
                deck.removeTopCard();
            }
        }
    }

    public static boolean askForCard(int player, ERank rank){
        for(int i = 0; i < players[player].getHand().size(); i++) {
            if(players[player].getHand().get(i).getRank() == rank){
                return true;
            }
        }
        return false;
    }
    public static void takeCard(int receiver, int giver, ERank rank){
        for(int i = 0; i < players[giver].getHand().size(); i++){
            Card card = players[giver].getHand().get(i);
            if(card.getRank() == rank){
                players[giver].removeFromHand(card);
                players[receiver].addToHand(card);
            }
        }
    }
    public static int[] checkHandForBook(int player){
        ArrayList<Integer> books = new ArrayList<>();
        for(int i = 1; i < 14; i++){
            int num = 0;
            for(int j = 0; j < players[player].getHand().size(); j++){
                if(players[player].getHand().get(j).getRankValue() == i){
                    num++;
                }
            }
            if(num == 4){
                books.add(i);
            }
        }
        return books.stream().mapToInt(i -> i).toArray();
    }
    public static ArrayList<Card> book(ERank rank, int player){
        ArrayList<Card> book = new ArrayList<>();
        for(int i = 0; i < players[player].getHand().size(); i++){
            if(players[player].getHand().get(i).getRank() == rank){
                book.add(players[player].getHand().get(i));
                players[player].getHand().remove(i);
            }
        }
        players[player].addToScore(1);
        return book;
    }

    public static Player[] getPlayers() {
        return players;
    }

    public static void setPlayers(Player[] players) {
        GoFish.players = players;
    }

    public static int[] getScores() {
        return scores;
    }

    public static void setScores(int[] scores) {
        GoFish.scores = scores;
    }
    public static void addToScore(int pos, int score){
        scores[pos] += score;
    }
    public static Deck getDeck() {
        return deck;
    }

    public static void setDeck(Deck deck) {
        GoFish.deck = deck;
    }

    public static int getTurn() {
        return turn;
    }

    public static void setTurn(int turn) {
        GoFish.turn = turn;
    }
    public static void nextTurn(){
        turn++;
    }
}
