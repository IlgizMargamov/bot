package com.company.card;

public enum Suit {
    NOT_STATED("notStated"),
    DIAMOND("diamond"),
    CLUBS("clubs"),
    HEARTS("hearts"),
    SPADES("spades");

    private final String val;

    Suit(String val) {
        this.val = val;
    }

    @Override
    public String toString() {
        return val;
    }
}
