package com.common.gamelogic;

/**
 * Варианты ответа пользователю
 */
public enum AnswerToPlayer {
    CHOOSE_SUIT("Choose a next card suit"),
    DOES_PLAYER_END("Is it all? y/n"),
    END_OF_TURN("Your turn has ended"),
    MAKE_TURN(" make your turn(type number of command)"),
    NOTHING("Nothing"),
    NOT_POSSIBLE_TURN("Not possible turn!"),
    NOW_PLAY("Now play: "),
    PLAYER("Player "),
    START_OF_SET("It's only start of set!"),
    TABLE_EMPTY("Table is empty"),
    TABLE_FULL("Table is full"),
    TRY_ANOTHER_CARD("Try another card"),
    WHAT_THROW("What card you want to throw?"),
    WHERE_THROW("Where you want to throw it?"),
    YOU_TAKE_5_CARD("You take 5 card"),
    YOU_TAKE_2_CARD("You take 2 card"),
    SKIP_THE_TURN("You skip the turn");

    private final String msg;

    AnswerToPlayer(String msg) {
        this.msg = msg;
    }

    /**
     * Получение сообщения пользователю
     * @return сообщение пользователю
     */
    public String getMsg() {
        return msg;
    }
}
