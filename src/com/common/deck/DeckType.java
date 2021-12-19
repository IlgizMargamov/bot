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

    /**
     * Get deckType by number of cards
     * @param deckTypeInt input number of cards from user
     * @return type of deck to play
     */
    public static DeckType getDeckType(int deckTypeInt) throws IllegalStateException {
        return switch (deckTypeInt) {
            case 24 -> DeckType.SMALL;
            case 36 -> DeckType.MEDIUM;
            case 52 -> DeckType.BIG;
            default -> throw new IllegalStateException("Unexpected value: " + deckTypeInt);
        };
    }

    /**
     * Get deckType by name of deck type
     * @param deckTypeString input number of cards from user
     * @return type of deck to play
     */
    public static DeckType getDeckType(String deckTypeString) throws IllegalStateException {
        return switch (deckTypeString) {
            case "SMALL" -> DeckType.SMALL;
            case "MEDIUM" -> DeckType.MEDIUM;
            case "BIG" -> DeckType.BIG;
            default -> throw new IllegalStateException("Unexpected value: " + deckTypeString);
        };
    }
}
