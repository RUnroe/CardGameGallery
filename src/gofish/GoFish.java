package gofish;

import gofish.presenters.GoFishGameScene;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import models.Card;
import models.Deck;
import models.ERank;
import models.Player;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

public class GoFish {
    private static Player[] players;
    private static int[] scores;
    private static Deck deck;
    private static int turn;
    private static ArrayList<ArrayList<Card>>[] allBooks;
    private static Stage rootStage;
    private static GoFishGameScene gameScene;

    private GoFish(){}

    public static void setup(Player... p) {
        turn = 0;
        players = p;
        scores = new int[players.length];
        deck = new Deck(1);
        allBooks = new ArrayList[players.length];
        for(int i = 0; i < players.length; i++){
            allBooks[i] = new ArrayList<>();
        }
        int l = players.length < 4 ? 7 : 5;
        for(int i = 0; i < l; i++){
            for(int j = 0; j < players.length; j++){
                players[j].addToHand(deck.getTopCard());
                deck.removeTopCard();
            }
        }
        setupGameScene();
    }

    private static void setupGameScene() {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(Objects.requireNonNull(GoFish.class.getResource("../gofish/views/go-fish-game-scene.fxml")));
        try {
            rootStage.setScene(new Scene(fxmlLoader.load()));
            gameScene = fxmlLoader.getController();
            gameScene.updateCurrentPlayerDisplay(players[getPlayerTurn()]);
            gameScene.setupSpinners();
        } catch (IOException e) {
            e.printStackTrace();
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
            if (!books.contains(i)) {

                int num = 0;
                for(int j = 0; j < players[player].getHand().size(); j++){
                    if(players[player].getHand().get(j).getRankValue() == i){
                        num++;
                    }
                }
                if(num == 4){
                    books.add(i);
                    players[player].setScore(players[player].getScore()+2);
                    int finalI = i;
                    new ArrayList<Card>(players[player].getHand()).forEach(card -> {
                        if (card.getRankValue() == finalI) {
                            players[player].getHand().remove(card);
                        }
                    });
                }
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

    public static int getPlayerTurn() {
        return turn % players.length;
    }

    public static Stage getRootStage() {
        return rootStage;
    }

    public static void setRootStage(Stage rootStage) {
        GoFish.rootStage = rootStage;
    }

    //    public static updateCurrentPlayerDisplay() {
//        Player currentPlayer = players[getPlayerTurn()];
//    }

    public static void setTurn(int turn) {
        GoFish.turn = turn;
    }
    public static void nextTurn(){
        turn++;
    }

    public static String[] aiTurn(){
        ArrayList<String> messages = new ArrayList<>();
        Player ai = players[turn % players.length];
        Card card = ai.getHand().get(new Random().nextInt(ai.getHand().size()));
        int opponent = -999;
        do {
            opponent = new Random().nextInt(players.length);
        } while (opponent != (turn % players.length));
        messages.add(ai.getName() + "asked " + players[opponent].getName() + " for " + card.getRankAsString() + "s");
        if(askForCard(opponent, card.getRank())){
            messages.add(players[opponent].getName() + "had (a) " + card.getRankAsString() + "(s)");
            takeCard(turn % players.length, opponent, card.getRank());
            int[] books = checkHandForBook(turn % players.length);
            if(books[0] != 0){
                for(int i : books){
                    messages.add(ai.getName() + " booked" + ERank.values()[i]);
                    ArrayList<Card> book = book(ERank.values()[i], turn % players.length);
                    allBooks[turn % players.length].add(book);
                }
            } else {
                messages.add(ai.getName() + " had no books");
            }
        } else {
            messages.add(players[opponent].getName() + " had no " + card.getRankAsString() + "s");
        }
        String[] mess = new String[messages.size()];
        for(int i = 0; i < messages.size(); i++){
            mess[i] = messages.get(i);
        }
        return mess;
    }
}
