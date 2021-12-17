package com.common.gamelogic;

import com.common.card.CardImpl;
import com.common.deck.Deck;
import com.common.player.BasePlayer;

import java.util.ArrayList;
import java.util.Scanner;

public abstract class BaseGameLogic implements GameLogic {

    protected Deck deck;
    protected final BasePlayer[] players;
    protected int currentPlayer;
    protected Scanner scanner = new Scanner(System.in);

    public BaseGameLogic(BasePlayer[] players, Deck deck) {
        this.deck = deck;
        this.players = players;
        currentPlayer = 0;
    }

    /**
     * Определяет с какого игрока начнётся игра
     * @return возвращает номер игрока в массиве
     */
    protected abstract int defineFirstPlayer();

    /**
     * Проверяет возможность такого хода игроком
     * @param card Какой картой собирается сходить игрок
     * @return true если возможен, false если невозможен
     */
    protected abstract boolean checkMoveCorrectness(CardImpl card);

    /**
     * Определяет закончилась ли игра
     * @return true, закончилась, false ещё не закончилась
     */
    protected abstract boolean defineEndOfGame();

    /**
     * Начало нового сета в игре
     * @return true если игрок взял карты на руку
     */
    protected abstract boolean startSet();

    /**
     * Сдвигает указатель на текующего плеера на count шагов
     * @param count насколько сдвинуть
     */
    protected void movePlayerOn(int count) {
        currentPlayer += count;
        currentPlayer %= players.length;
    }

    /**
     * Создаёт руку для игрока размераа count
     * @param count  размер руки
     * @return возвращает руку
     */
    protected ArrayList<CardImpl> createHand(int count) {
        ArrayList<CardImpl> hand = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            hand.add(deck.giveNext());
        }
        return hand;
    }

    /**
     * Раздать игрокам по count карт
     * @param count сколько карт каждому игроку
     */
    protected void giveCardToPlayers(int count) {
        for (BasePlayer player : players) {
            player.TakeHand(createHand(count));
        }
    }

    /**
     * Используется для отправки сообщения пользователю
     * @param message содержание одного сообщения
     */
    protected void sendToUser(String[] message) {
        for (String msg : message) {
            System.out.println(msg);
        }
        System.out.println();
    }

    /**
     * Получить информацию от пользователя
     * @return возвращает сообщение от пользователя
     */
    protected String getFromUser(){
        return scanner.nextLine();
    }

    protected enum EndOfGame{
        Win("You Won"),
        Lose("You Lose"),
        Tie("Tie");

        String msg;

        EndOfGame(String msg){
            this.msg = msg;
        }

        public String getMsg() {
            return msg;
        }
    }

    protected enum AnswerToPlayer {
        NotPossibleTurn("Not possible turn!"),
        StartOfSet("It's only start of set!"),
        WhereThrow("Where you want to throw it?"),
        WhatThrow("What card you want to throw?"),
        DoesPlayerEnd("Is it all? y/n"),
        TryAnotherCard("Try another card"),
        TableEmpty("Table is empty"),
        TableFull("Table is full"),
        Nothing("Nothing"),
        Player("Player "),
        MakeTurn(" make your turn(type number of command)"),
        ChooseSuit("Choose a next card suit");

        private final String msg;

        AnswerToPlayer(String msg){
            this.msg = msg;
        }

        public String getMsg(){
            return msg;
        }
    }
}