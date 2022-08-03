package com.games;

/**
 * Тип хода по вводу от пользователя
 */
public enum TypeOfCommand {
    BACK("0. Back"),
    CHECK_HAND("1. What on my hand?"),
    CHECK_TABLE("2. What on the table?"),
    CHECK_TRUMP("3. What is trump?"),
    THROW_CARD("4. Throw this card."),
    PASS("5. Pass"),
    TAKE("6. Take"),
    YES("7. Yes"),
    NO("8. No"),
    DECK_SIZE("9. How many card in deck?"),
    NOTHING("Nothing");

    private final String type;

    TypeOfCommand(String type){
        this.type = type;
    }

    /***
     * Получение строкового представления типа хода
     * @return тип хода строкой
     */
    public String getType() {
        return type;
    }

    /**
     * Получение типа хода по команде
     * @param numberOfCommand номер команды
     * @return ход
     */
    public static TypeOfCommand pickTurn(int numberOfCommand){
        switch (numberOfCommand){
            case 0 -> {return BACK;}
            case 1 -> {return CHECK_HAND;}
            case 2 -> {return CHECK_TABLE;}
            case 3 -> {return CHECK_TRUMP;}
            case 4 -> {return THROW_CARD;}
            case 5 -> {return PASS;}
            case 6 -> {return TAKE;}
            case 7 -> {return YES;}
            case 8 -> {return NO;}
            case 9 -> {return DECK_SIZE;}
            default -> {return NOTHING;}
        }
    }
}