package com.company;

import com.company.card.*;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.Arrays;

public class DeckImpl implements Deck {
    public Card[] Cards;

    public DeckImpl(/*int cardCount*/) {
        Cards = getSortedDeck(/*cardCount*/);
    }

    @Override
    public String toString() {
        return "DeckImpl{" +
                "Cards=" + Arrays.toString(Cards) +
                '}';
    }

    private Card[] getShuffledDeck(Shuffler shuffler){
        // TODO
        throw new NotImplementedException();
    }

    private Card[] getSortedDeck(/*int cardCount*/) {
        int cardCount=36; // may be 52, 24
        Card[] deck = new Card[cardCount];
        int i = 0;
        for (Suit suit : Suit.values()) {
            if (suit == Suit.HIDDEN) continue;
            for (Rank rank : Rank.values()) {
                if (rank == Rank.HIDDEN) continue;

                if (checkCardDeckConditions(cardCount, 36, rank.ordinal(), 5)) continue;
                if (checkCardDeckConditions(cardCount, 24, rank.ordinal(), 8)) continue;

                deck[i] = new CardImpl(suit, rank);
                i++;
            }
        }
        return deck;
    }

    private boolean checkCardDeckConditions(int cardCount, int exceptionalCardCount, int rankOrdinal, int cardCountControl){
        return isCardCountExceptional(cardCount, exceptionalCardCount) &&
                isOrdinalBigEnough(rankOrdinal, cardCountControl);
    }

    private boolean isCardCountExceptional(int cardCount, int exceptionalCardCount){
        return cardCount == exceptionalCardCount;
    }

    private boolean isOrdinalBigEnough(int cardOrdinal, int cardOrdinalControl){
        return cardOrdinal < cardOrdinalControl;
    }
}
