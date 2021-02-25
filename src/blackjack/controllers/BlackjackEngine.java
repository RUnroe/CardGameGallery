package blackjack.controllers;

import models.CardComparator;
import models.Deck;
import models.Player;

public class BlackjackEngine {

    public void setup() {
        Player don = new Player("Don");
        Deck deck = new Deck(1);
        for (int i = 0; i < 10; i++) {
            System.out.println("Top card: " + deck.getTopCard().getName());
            don.drawFromDeck(deck);
            System.out.println("Don's hand: ");
            don.getHand().forEach(card -> System.out.println('\t' + card.getName()));
            System.out.println("---------------------------");
        }
        don.getHand().sort(new CardComparator());
        System.out.println("Don's hand sorted by rank: ");
        don.getHand().forEach(card -> {
            System.out.println('\t' + card.getName());
        });
    }
}
