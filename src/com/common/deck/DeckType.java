package com.common.deck;

public enum DeckType {
    SMALL(24),
    MEDIUM(36),
    BIG(52);

    private final int m_cards;

    DeckType(int cards) {
        m_cards=cards;
    }

    public int getCardsCount(){
        return this.m_cards;
    }
}
