package com.common.deck;

/**
 * Размер колоды
 */
public enum DeckType {
    SMALL(24),
    MEDIUM(36),
    BIG(52);

    private final int m_cards;

    DeckType(int cards) {
        m_cards=cards;
    }

    /**
     * Получение числа карт в колоде
     * @return число карт в колоде
     */
    public int getCardsCount(){
        return this.m_cards;
    }
}
