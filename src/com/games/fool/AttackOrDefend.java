package com.games.fool;

/**
 * Тип хода пользователя
 */
public enum AttackOrDefend {
    ATTACK("You Attack"),
    DEFEND("You Defend");

    private final String msg;

    AttackOrDefend(String msg) {
        this.msg = msg;
    }

    /**
     * Выдача сообщения пользователю
     *
     * @return строка для вывода пользователю
     */
    public String getMsg() {
        return msg;
    }
}