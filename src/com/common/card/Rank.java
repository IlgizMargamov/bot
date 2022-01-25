package com.common.card;

/**
 * Тип карты*/
public enum Rank {
    HIDDEN("Hidden",0),
    TWO("2", -600),
    THREE("3", -500),
    FOUR("4", -400),
    FIVE("5",-300),
    SIX("6", -200),
    SEVEN("7", -100),
    EIGHT("8", 0),
    NINE("9", 100),
    TEN("10", 200),
    JACK("Jack", 300),
    LADY("Lady", 400),
    KING("King", 500),
    ACE("Ace", 600);

    private final String rank;
    public final int cost;

    Rank(String rank, int cost){
        this.rank = rank;
        this.cost = cost;
    }

    /**Получить ранг карты*/
    public String getRank() {
        return rank;
    }
}