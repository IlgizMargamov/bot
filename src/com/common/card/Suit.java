package com.common.card;

/**
 * Масть карты*/
public enum Suit {
    CLUBS("1. Clubs"),
    DIAMOND("2. Diamond"),
    HEARTS("3. Hearts"),
    HIDDEN("Hidden"),
    SPADES("4. Spades");

    private final String suit;

    Suit(String suit) {
        this.suit = suit;
    }

    /**
     * Получить масть */
    public String getSuit() {
        return suit;
    }

    /**
     * Получить масть по номеру*/
    public static Suit valuesOf(int number) {
        switch (number) {
            case 1 -> {
                return CLUBS;
            }
            case 2 -> {
                return DIAMOND;
            }
            case 3 -> {
                return HEARTS;
            }
            case 4 -> {
                return SPADES;
            }
            default -> {
                return HIDDEN;
            }
        }
    }
}