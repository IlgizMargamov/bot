package com.common.card;

public class CardImpl implements Card {
    public Suit CardSuit;
    public Rank CardRank;

    public CardImpl(){
        CardSuit=Suit.HIDDEN;
        CardRank= Rank.HIDDEN;
    }

    public CardImpl(Suit suit, Rank rank){
        if (suit==Suit.HIDDEN || rank== Rank.HIDDEN) throw new IllegalArgumentException();
        CardSuit=suit;
        CardRank=rank;
    }

    @Override
    public String toString() {
        return "CardImpl{" +
                "CardSuit=" + CardSuit +
                ", CardRank=" + CardRank +
                '}';
    }
}
