package com.company.card;

public class CardImpl implements Card {
    public Suit CardSuit;
    public Rank CardRank;

    public CardImpl(){
        CardSuit=Suit.NOT_STATED;
        CardRank=Rank.NOT_STATED;
    }

    public CardImpl(Suit suit, Rank rank){
        if (suit==Suit.NOT_STATED || rank==Rank.NOT_STATED) throw new IllegalArgumentException();
        CardSuit=suit;
        CardRank=rank;
    }
}
