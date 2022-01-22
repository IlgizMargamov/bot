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
    HAVE_CLOSED_LOBBY(" have closed the lobby"),
    CHOOSE_LOBBY_PIN("Choose lobby pin"),
    HOW_TO_PLAY_FOOL("""
Правила игры "Дурак"
Первым ходит игрок с самым младшим козырем.
Если на руках данной масти нет, решают по самой младшей карте.
Когда игроков больше двух, ходят по часовой стрелке, то есть к сидящему слева.
Он кладет одну или две карты одного значения. Второй игрок должен их отбить.
Это значит, что он может перекрыть их картой той же масти, но старшей по значению, или козырем.
Козырную карту может побить только козырь большего значения.
Если карты отбиты, они уходят в отбой, и до конца игры их не используют.
Запрещается открывать отбой, чтобы узнать, какие карты уже вне игры.
Если игрок не смог или не захотел отбивать ход, он забирает все карты с кона – таковы правила в игре «Дурак».
"""),
    HOW_TO_PLAY_PHARAOH("""
Правила игры "101"
В игру играют от 2-5 человек. Каждому раздается по 4 карты.
Последнюю карту раздающий ложит на стол, это его первый ход.
Далее все предельно просто, играющие покрывают карту на столе либо картой той же достоинства, либо той же масти, либо дамой.
Если таких карт нет на руках, берётся карта с колоды. Если опять нечем покрыть, право хода передается следующему.
6 -эту карту надо срочно накрыть! Если нечем, карту берут с колоды до тех пор, пока не найдется, чем накрыть.
7 - следующий после Вас игрок берет две карты с колоды и пропускает ход, если это 7 пик, то следующий игрок берет четыре карты.
Дама - с помощью дамы можно заказать любую масть. Даму можно положить на любую карту, после дамы - также любая, но только заказанной масти.
Король пик - следующий игрок берет 5 карт и пропускает ход, король любой другой масти - следующий игрок берет четыре карты и ходит.
Туз - ход пропускает следующий после вас игрок. Очередь третьего.
Первичная цель - избавиться от всех карт как можно быстрее, вторичная - набрать как можно меньше очков.
Первый раунд игры продолжается до тех пор, пока один из игроков не останется без карт.
Тогда остальные начинают считать очки. После подсчета начинается второй раунд.
После второго - опять подсчет очков, которые суммируются.
Тот, кто набрал за несколько раундов более ста очков, выбывает.
Если же он набрал ровно 101 - все его очки обнуляются!
Победителем становится тот, кто набрал меньше всех очков и единственный остался в игре.
            """),
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
