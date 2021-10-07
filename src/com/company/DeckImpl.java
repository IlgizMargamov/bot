package com.company;

import com.company.card.*;

import java.util.Arrays;

public class DeckImpl implements Deck {
    public Card[] Cards;

    public DeckImpl(int cardCount) {
        Cards = getSortedDeck(cardCount);
    }

    @Override
    public String toString() {
        return "DeckImpl{" +
                "Cards=" + Arrays.toString(Cards) +
                '}';
    }

    private Card[] getSortedDeck(int cardCount) {
        Card[] deck = new Card[cardCount];
        int i = 0;
        for (Suit suit : Suit.values()) {
            if (suit == Suit.HIDDEN) continue;
            for (Rank rank : Rank.values()) {
                if (rank == Rank.HIDDEN) continue;
                if (cardCount == 36) {
                    if (rank.ordinal() < 5) continue;
                }
                deck[i] = new CardImpl(suit, rank);
                i++;
            }
        }
        return deck;
    }
}
