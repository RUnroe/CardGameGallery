package gofish;

import gofish.presenters.GoFishGameScene;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import models.*;

import java.io.IOException;
import java.util.*;

public class GoFish {
    private static Player[] players;
    private static int[] scores;
    private static Deck deck;
    private static int turn;
    private static List<ERank> completedBooks;
    private static Stage rootStage;

    private GoFish(){}

    public static void setup(Player... p) {
        turn = 0;
        players = p;
        scores = new int[players.length];
        deck = new Deck(1);
        completedBooks = new ArrayList<>();
//        for(int i = 0; i < players.length; i++){
//            completedBooks[i] = new ArrayList<>();
//        }
        int l = players.length < 4 ? 7 : 5;
        for (int i = 0; i < l; i++) {
            for (int j = 0; j < players.length; j++) {
//                players[j].addToHand(new Card(ERank.QUEEN, ESuit.HEARTS));
                players[j].drawFromDeck(deck);
            }
        }
        setupGameScene();
    }

    private static void setupGameScene() {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(Objects.requireNonNull(GoFish.class.getResource("../gofish/views/go-fish-game-scene.fxml")));
        try {
            rootStage.setScene(new Scene(fxmlLoader.load()));
            GoFishGameScene gameScene = fxmlLoader.getController();
            Arrays.stream(players).forEach(player -> player.getHand().sort(new CardComparator()));
            gameScene.updateDisplay(players[getPlayerTurn()]);
            gameScene.updateSpinners();
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

    public static void takeCard(int receiver, int giver, ERank rank) {
        for (int i = 0; i < players[giver].getHand().size(); i++) {
            Card card = players[giver].getHand().get(i);
            if (card.getRank() == rank) {
                players[giver].removeFromHand(card);
                players[receiver].addToHand(card);
            }
        }
    }

    public static void checkHandForBook(int player) {
//        ArrayList<Integer> completedBooks = new ArrayList<>();
        for (ERank rank : ERank.values()) {
            if (!completedBooks.contains(rank)) {
                int num = 0;
                for (int j = 0; j < players[player].getHand().size(); j++) {
                    if (players[player].getHand().get(j).getRank().equals(rank)) {
                        num++;
                    }
                }
                if (num >= 4) {
                    System.out.println("Book of " + rank.name().toLowerCase() + "s found!");
                    completedBooks.add(rank);
                    players[player].setScore(players[player].getScore() + 2);
                    new ArrayList<Card>(players[player].getHand()).forEach(card -> {
                        if (card.getRank().equals(rank)) {
                            players[player].getHand().remove(card);
                        }
                    });
                }
            }
        }
//        return completedBooks.stream().map(i -> i).toArray();
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

    public static boolean aiTurn() {
        ArrayList<String> messages = new ArrayList<>();
        Player ai = players[getPlayerTurn()];
        Card card;
        if (ai.getHand().size() > 0) {
            card = ai.getHand().get(new Random().nextInt(ai.getHand().size()));
        } else {
            return false;
        }
        boolean isCardFound;
        int opponent;
        do {
            opponent = new Random().nextInt(players.length);
        } while (opponent == (getPlayerTurn()));
        messages.add(ai.getName() + "asked " + players[opponent].getName() + " for " + card.getRankAsString() + "s");
        isCardFound = askForCard(opponent, card.getRank());
        if (isCardFound) {
            messages.add(players[opponent].getName() + "had (a) " + card.getRankAsString() + "(s)");
            takeCard(getPlayerTurn(), opponent, card.getRank());
            checkHandForBook(getPlayerTurn());
//            if(books[0] != 0){
//                for(int i : books){
//                    messages.add(ai.getName() + " booked" + ERank.values()[i]);
//                    ArrayList<Card> book = book(ERank.values()[i], turn % players.length);
//                    allBooks[turn % players.length].add(book);
//                }
//            } else {
//                messages.add(ai.getName() + " had no books");
//            }
        } else {
            messages.add(players[opponent].getName() + " had no " + card.getRankAsString() + "s");
//            nextTurn();
        }
        String[] mess = new String[messages.size()];
        for (int i = 0; i < messages.size(); i++) {
            mess[i] = messages.get(i);
        }
        return isCardFound;
    }

    public static List<ERank> getCompletedBooks() {
        return completedBooks;
    }

    public static void setCompletedBooks(List<ERank> completedBooks) {
        GoFish.completedBooks = completedBooks;
    }
}
