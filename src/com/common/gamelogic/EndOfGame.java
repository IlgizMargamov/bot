package com.common.gamelogic;

/**
 * Варианты причины окончания игры
 */
public enum EndOfGame{
    WIN("You Won"),
    LOSE("You Lose"),
    TIE("Tie");

    String msg;

    EndOfGame(String msg){
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
