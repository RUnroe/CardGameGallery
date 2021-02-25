package models;

import java.util.Comparator;

public class CardComparator implements Comparator<Card> {
    @Override
    public int compare(Card cardA, Card cardB) {
        int sortValue = cardA.getRankValue() - cardB.getRankValue();
        if (sortValue == 0) {
            sortValue = cardA.getSuitValue() - cardB.getSuitValue();
        }
        return sortValue;
    }
}
