package com.games;

public enum TypeOfTurn {
    BACK("0. Back"),
    CHECK_HAND("1. What on my hand?"),
    CHECK_TABLE("2. What on the table?"),
    CHECK_TRUMP("3. What is trump?"),
    THROW_CARD("4. Throw this card."),
    PASS("5. Pass"),
    TAKE("6. Take"),
    NOTHING("Nothing");

    private final String type;

    TypeOfTurn(String type){
        this.type = type;
    }

    public String getString() {
        return type;
    }

    public static TypeOfTurn pickTurn(String command){
        switch (command){
            case "0" -> {return BACK;}
            case "1" -> {return CHECK_HAND;}
            case "2" -> {return CHECK_TABLE;}
            case "3" -> {return CHECK_TRUMP;}
            case "4" -> {return THROW_CARD;}
            case "5" -> {return PASS;}
            case "6" -> {return TAKE;}
            default -> {return NOTHING;}
        }
    }
}
