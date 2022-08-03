package com.games.fool;

import com.common.card.CardImpl;
import com.common.gamelogic.AnswerToPlayer;

import java.util.Objects;

/**
 * Сущность, определяющая корректность покрытия карты
 */
public class TupleOfCard {
    private final FoolLogic foolLogic;
    public CardImpl firstCard;
    public CardImpl secondCard;

    /**
     * Создание кортежа
     *
     * @param foolLogic логика, по которой судить
     * @param first     карта
     */
    public TupleOfCard(FoolLogic foolLogic, CardImpl first) {
        this.foolLogic = foolLogic;
        this.firstCard = first;
    }

    /**
     * Покрывает ли вторая карта
     *
     * @param second вторая карта
     * @return покрывает или нет
     */
    public boolean isCover(CardImpl second) {
        if (firstCard.CardSuit == foolLogic.trump.CardSuit)
            return second.CardSuit == foolLogic.trump.CardSuit && firstCard.CardRank.ordinal() < second.CardRank.ordinal();
        if (second.CardSuit == foolLogic.trump.CardSuit) return true;
        return firstCard.CardRank.ordinal() < second.CardRank.ordinal() && firstCard.CardSuit == second.CardSuit;
    }

    /**
     * Покрытие карты
     *
     * @param card покрытие карты
     */
    public void coverWithCard(CardImpl card) {
        this.secondCard = card;
    }

    public String toString() {
        if (secondCard == null) return firstCard.cardSuitAndRank() + " \\ " + AnswerToPlayer.NOTHING.getMsg();
        return firstCard.cardSuitAndRank() + " \\ " + secondCard.cardSuitAndRank();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TupleOfCard tuple = (TupleOfCard) o;
        return firstCard.equals(tuple.firstCard) && Objects.equals(secondCard, tuple.secondCard);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstCard, secondCard);
    }
}
