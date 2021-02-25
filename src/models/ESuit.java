package models;

public enum ESuit {
    DIAMONDS(1),
    CLUBS(2),
    HEARTS(3),
    SPADES(4);

    private final int value;

    ESuit(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
