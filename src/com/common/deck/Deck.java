package com.common.deck;

import com.common.card.Card;
import com.common.card.CardImpl;
import com.common.card.Rank;
import com.common.card.Suit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck {
    public List<Card> Cards;

    public Deck(DeckType deckType) {
        int cardsCount = deckType.getCardsCount();
        Cards = getSortedDeck(cardsCount);

        shuffle(Cards);
    }

    @Override
    public String toString() {
        return "DeckImpl{" +
                "Cards=" + Cards.toString() +
                '}';
    }

    private void shuffle(List<Card> cards) {
        Collections.shuffle(cards);
    }

    private List<Card> getSortedDeck(int length) {
        List<Card> cards = new ArrayList<Card>(length);
        int i = 0;
        for (Suit suit : Suit.values()) {
            if (suit == Suit.HIDDEN) continue;
            for (Rank rank : Rank.values()) {
                if (rank == Rank.HIDDEN) continue;

                if (checkCardDeckConditions(length, 36, rank.ordinal(), 5)) continue;
                if (checkCardDeckConditions(length, 24, rank.ordinal(), 8)) continue;

                cards.add(new CardImpl(suit, rank));
                i++;
            }
        }
        return cards;
    }

    private boolean checkCardDeckConditions(int cardCount, int exceptionalCardCount, int rankOrdinal, int cardCountControl) {
        return isCardCountExceptional(cardCount, exceptionalCardCount) &&
                isOrdinalBigEnough(rankOrdinal, cardCountControl);
    }

    private boolean isCardCountExceptional(int cardCount, int exceptionalCardCount) {
        return cardCount == exceptionalCardCount;
    }

    private boolean isOrdinalBigEnough(int cardOrdinal, int cardOrdinalControl) {
        return cardOrdinal < cardOrdinalControl;
    }
}
