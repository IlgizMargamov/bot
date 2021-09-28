package com.company;

import com.company.card.Card;

public class DeckImpl implements Deck{
    public Card[] Deck;
    public int CardsCount;

    public DeckImpl(){
        Deck = getSortedDeck();
    }

    private Card[] getSortedDeck() {
        return new Card[0];
    }
}
