package com.games.pharaoh;

import com.common.card.CardImpl;
import com.common.card.Rank;
import com.common.card.Suit;
import com.common.player.BasePlayer;

import java.util.HashMap;

public class PharaohAI extends BasePlayer {

    public PharaohAI(String name) {
        super(name);
    }

    /**
     * Метод для выбора карты которой сходит бот
     *
     * @param currentCard карта которой последней сходили
     * @return картой которой сходить
     */
    public CardImpl Turn(CardImpl currentCard) {
        HashMap<CardImpl, Integer> possibleTurn = countPossibleScoreLose(currentCard);
        CardImpl forTurn = null;
        if(!possibleTurn.isEmpty()) {
            int max = Integer.MIN_VALUE;
            for (CardImpl card : possibleTurn.keySet()) {
                if (possibleTurn.get(card) > max) forTurn = card;
            }
            hand.remove(forTurn);
        }
        return forTurn;
    }

    /**
     * Метод для выборки возможных ходов
     *
     * @param currentCard Карта которая сейчас лежит на столе
     * @return Сколько очков потеряет игрок за использование каждой карты на руке
     */
    private HashMap<CardImpl, Integer> countPossibleScoreLose(CardImpl currentCard) {
        HashMap<CardImpl, Integer> ans = new HashMap<>();
        for (CardImpl card : this.hand) {
            if (currentCard.CardRank != card.CardRank && currentCard.CardSuit != card.CardSuit && currentCard.CardRank != Rank.LADY) {
                continue;
            }
            int cardScore;
            switch (card.CardRank) {
                case JACK -> cardScore = 2;
                case LADY -> cardScore = 3;
                case KING -> cardScore = 4;
                case ACE -> cardScore = 11;
                default -> cardScore = card.CardRank.ordinal();
            }
            ans.put(card, cardScore);
        }
        return ans;
    }


    public Suit pickSuit() {
        HashMap<Suit, Integer> countCard = new HashMap<>();
        countCard.put(Suit.CLUBS, 0);
        countCard.put(Suit.DIAMOND, 0);
        countCard.put(Suit.HEARTS, 0);
        countCard.put(Suit.SPADES, 0);

        for (CardImpl card : hand) countCard.replace(card.CardSuit, countCard.get(card.CardSuit) + 1);
        int max = Integer.MIN_VALUE;
        Suit pickedSuit = Suit.CLUBS;
        for (Suit suit : countCard.keySet()) {
            if (countCard.get(suit) > max) pickedSuit = suit;
        }
        return pickedSuit;
    }
}
