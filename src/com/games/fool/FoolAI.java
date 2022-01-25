package com.games.fool;

import com.common.card.CardImpl;
import com.common.card.Rank;
import com.common.card.Suit;
import com.common.player.BasePlayer;

import java.util.ArrayList;
import java.util.HashMap;

public class FoolAI extends BasePlayer {
    /**
     * Метод вызываемой при атаке бота
     *
     * @param cardsOnTable Карты которые лежат на столе
     * @param trump        Козырь
     * @return Возвращает карту, которой хочет атаковать
     */
    public CardImpl attackTurn(ArrayList<CardImpl> cardsOnTable, Suit trump) {
        if (cardsOnTable.size() == 0) {
            return chooseMinorCard(trump);
        }
        ArrayList<CardImpl> possibleTurn = getAllPossibleAttack(cardsOnTable);
        HashMap<Integer, CardImpl> cardsForTurn = calculateCardScore(trump, possibleTurn);
        hand.remove(cardsForTurn.get(getMin(cardsForTurn)));
        return cardsForTurn.get(getMin(cardsForTurn));
    }

    /**
     * Методы вызываемый для защиты бота
     *
     * @param cardsOnTable Карты которые лежат на столе
     * @param trump        Козырь
     * @return Возвращает map. Первая карта куда положить, вторая какую карту с руки положить.
     * Возвращает null если берёт карты.
     */
    public HashMap<CardImpl, CardImpl> defendTurn(ArrayList<CardImpl> cardsOnTable, Suit trump) {
        HashMap<Integer, CardImpl> cardOnTableScore = calculateCardScore(trump, cardsOnTable);
        HashMap<CardImpl, CardImpl> defend = new HashMap<>();
        for (int i = 0; i < cardsOnTable.size(); i++) {
            int score = getMin(cardOnTableScore);
            CardImpl cardOnTable = cardOnTableScore.get(score);
            CardImpl forDefend = chooseCardForDefend(cardOnTable, trump);
            if (forDefend == null) {
                return null;
            }
            defend.put(cardOnTable, forDefend);
            cardOnTableScore.remove(score);
        }
        for (CardImpl cardOnTable:defend.keySet()){
            hand.remove(defend.get(cardOnTable));
        }
        return defend;
    }


    /**
     * @param cardOnTable Карты на столе
     * @param trump       Козырь
     * @return Возвращает минимальную карты для защиты
     */
    private CardImpl chooseCardForDefend(CardImpl cardOnTable, Suit trump) {
        HashMap<Integer, CardImpl> cardOnHandScore = calculateCardScore(trump, getAllPossibleDefend(cardOnTable, trump));
        return cardOnHandScore.get(getMin(cardOnHandScore));
    }


    /**
     * @param trump Козырь
     * @param cards Карты для которых надо посчитать их очки
     * @return Возвращает map, для каждого значения очков её карту
     */
    private HashMap<Integer, CardImpl> calculateCardScore(Suit trump, ArrayList<CardImpl> cards) {
        HashMap<Integer, CardImpl> cardsForTurn = new HashMap<>();
        for (CardImpl card : cards) {
            cardsForTurn.put(card.CardRank.cost + (card.CardSuit == trump ? 700 : 0), card);
        }
        return cardsForTurn;
    }


    /**
     * @param trump Козырь
     * @return Возвращает минимальную по очкам карту на руке
     */
    private CardImpl chooseMinorCard(Suit trump) {
        CardImpl minCard = new CardImpl(Suit.HIDDEN, Rank.HIDDEN);
        for (CardImpl card : hand) {
            if (card.CardRank.ordinal() > minCard.CardRank.ordinal() && card.CardSuit != trump) {
                minCard = card;
            }
        }
        if (minCard.CardSuit != Suit.HIDDEN) return minCard;
        else {
            for (CardImpl card : hand) {
                if (card.CardRank.ordinal() > minCard.CardRank.ordinal()) {
                    minCard = card;
                }
            }
        }
        return minCard;
    }


    /**
     * @param cardOnTable Карта для которой нужно найти список
     * @param trump       Козырь
     * @return Возвращает все возможные карты для защиты данной карты
     */
    private ArrayList<CardImpl> getAllPossibleDefend(CardImpl cardOnTable, Suit trump) {
        ArrayList<CardImpl> possibleTurn = new ArrayList<>();
        for (CardImpl cardOnHand : hand) {
            if (cardOnHand.CardSuit == cardOnTable.CardSuit && cardOnHand.CardRank.ordinal() > cardOnTable.CardRank.ordinal() ||
                    cardOnHand.CardSuit == trump && cardOnTable.CardSuit != trump) {
                possibleTurn.add(cardOnHand);
            }
        }
        return possibleTurn;
    }


    /**
     * @param cardsOnTable Карты на столе
     * @return Возвращет список всех возможных карты для атаки
     */
    private ArrayList<CardImpl> getAllPossibleAttack(ArrayList<CardImpl> cardsOnTable) {
        ArrayList<CardImpl> possibleTurn = new ArrayList<>();
        for (CardImpl cardOnHand : hand) {
            for (CardImpl cardOnTable : cardsOnTable) {
                if (cardOnTable.CardRank == cardOnHand.CardRank && !possibleTurn.contains(cardOnHand)) {
                    possibleTurn.add(cardOnHand);
                }
            }
        }
        return possibleTurn;
    }


    /**
     * @param map map гду нужно найти минимальный элемент
     * @return Ищет минимальный элемент в списке по числовому ключу
     */
    private int getMin(HashMap<Integer, CardImpl> map) {
        //noinspection OptionalGetWithoutIsPresent
        return map.keySet().stream().min(Integer::compareTo).get();
    }
}
