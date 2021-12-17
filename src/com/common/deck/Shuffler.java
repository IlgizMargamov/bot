package com.common.deck;

import com.common.card.Card;

/**
 * Интерфейс для перемешивальщика колоды
 */
public interface Shuffler {
    Card[] shuffle(Card[] cards);
}
