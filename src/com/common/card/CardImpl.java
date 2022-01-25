package com.common.card;

import java.util.Objects;

/**
 * Реализация карты
 */
public class CardImpl implements Card {
    public Suit CardSuit;
    public Rank CardRank;

    public CardImpl() {
        CardSuit = Suit.HIDDEN;
        CardRank = Rank.HIDDEN;
    }

    /**
     * Создание карты
     * @param suit масть карты
     * @param rank ранг карты
     */
    public CardImpl(Suit suit, Rank rank) {
        //if (suit == Suit.HIDDEN || rank == Rank.HIDDEN) throw new IllegalArgumentException();
        CardSuit = suit;
        CardRank = rank;
    }

    /**
     * Получение масти
     * @return масть
     */
    public Suit getCardSuit() {
        return CardSuit;
    }

    /**
     * Получение ранга
     * @return ранг
     */
    public Rank getCardRank() {
        return CardRank;
    }

    /**
     * Вся информация о карте
     * @return масть и ранг
     */
    public String cardSuitAndRank() {
        return CardSuit + " " + CardRank;
    }

    @Override
    public String toString() {
        return "CardImpl{" +
                "CardSuit=" + CardSuit +
                ", CardRank=" + CardRank +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CardImpl card = (CardImpl) o;
        return CardSuit == card.CardSuit && CardRank == card.CardRank;
    }

    @Override
    public int hashCode() {
        return Objects.hash(CardSuit, CardRank);
    }
}
