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
    INVITE_FRIENDS_USING_PIN("Invite your friends using your pin: "),
    YOU_HAVE_ENTERED("You have entered the @"),
    YOU_LEFT("You left from "),
    LOBBY(" lobby."),
    USE_BUTTONS("\nUse these buttons to navigate"),
    LOBBY_PIN("This lobby pin: "),
    CURRENT_PLAYERS("Current players in lobby: "),
    CHOOSE_TYPE_OF_DECK("Choose type of deck you want to play"),
    DECK_HAS_BEEN_SET("Deck type has been set to: "),
    BY(" by @"),
    GAME_HAS_STARTED("Game has started"),
    WRONG_COMMAND("Wrong command."),
    TRY_AGAIN("\nTry again,please"),
    NOT_YOUR_TURN("Not your turn yet."),
    LOBBY_CREATOR("Lobby creator: "),
    PLAYERS("Players: "),
    GAME("Game to play: "),
    DECK_TYPE("Deck type: "),
    OPEN_BRACE_LINE("{\n"),
    LINE("\n"),
    CLOSE_BRACE_LINE("}\n"),
    AT("@"),
    LOBBY_DELETED("Lobby deleted"),
    HAVE_ENTERED(" have entered the "),
    TRY_ASK_AGAIN("Try asking your friend the pin once again.\nYou typed: "),
    OR_CREATE_YOUR_OWN("\nOr create your own lobby"),
    HERE_AVAILABLE_COMMANDS("Here are your available commands"),
    PLEASE_ENTER_PIN("Please enter the pin from the game you want to enter"),
    YOU_TAKE_5_CARD("You take 5 card"),
    YOU_TAKE_2_CARD("You take 2 card"),
    SKIP_THE_TURN("You skip the turn"),
    WHO_SENT_THIS_CARD(" sent this card"),
    ;

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
