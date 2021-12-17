package com.common.gamelogic;

/**
 * Варианты ответа пользователю
 */
public enum AnswerToPlayer {
    NOT_POSSIBLE_TURN("Not possible turn!"),
    START_OF_SET("It's only start of set!"),
    WHERE_THROW("Where you want to throw it?"),
    WHAT_THROW("What card you want to throw?"),
    DOES_PLAYER_END("Is it all? y/n"),
    TRY_ANOTHER_CARD("Try another card"),
    TABLE_EMPTY("Table is empty"),
    TABLE_FULL("Table is full"),
    NOTHING("Nothing"),
    PLAYER("Player "),
    MAKE_TURN(" make your turn(type number of command)"),
    CHOOSE_SUIT("Choose a next card suit");

    private final String msg;

    AnswerToPlayer(String msg){
        this.msg = msg;
    }

    /**
     * Получение сообщения пользователю
     * @return сообщение пользователю
     */
    public String getMsg(){
        return msg;
    }
}
