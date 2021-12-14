package com.common.deck;

import com.common.card.CardImpl;
import com.common.card.Rank;
import com.common.card.Suit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck {
    private final List<CardImpl> Cards;

    public Deck(DeckType deckType) {
        int cardsCount = deckType.getCardsCount();
        Cards = getSortedDeck(cardsCount);
        shuffle(Cards);
    }

    public Deck(List<CardImpl> oldDeck){
        this.Cards = oldDeck;
        shuffle(this.Cards);
    }

    @Override
    public String toString() {
        return "DeckImpl{" +
                "Cards=" + Cards.toString() +
                '}';
    }

    private void shuffle(List<CardImpl> cards){
        Collections.shuffle(cards);
    }

    private List<CardImpl> getSortedDeck(int length) {
        List<CardImpl> cards= new ArrayList<>(length);
        for (Suit suit : Suit.values()) {
            if (suit == Suit.HIDDEN) continue;
            for (Rank rank : Rank.values()) {
                if (rank == Rank.HIDDEN) continue;

                if (checkCardDeckConditions(length, 36, rank.ordinal(), 5)) continue;
                if (checkCardDeckConditions(length, 24, rank.ordinal(), 8)) continue;

                cards.add(new CardImpl(suit, rank));
            }
        }
        return cards;
    }

    public CardImpl giveNext(){
        CardImpl card = Cards.get(Cards.size()-1);
        Cards.remove(Cards.size()-1);
        return card;
    }

    public boolean isEmpty(){
        return Cards.size() == 0;
    }

    private boolean checkCardDeckConditions(int cardCount, int exceptionalCardCount, int rankOrdinal, int cardCountControl) {
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