package com.common.gamelogic;

import com.Main;
import com.common.card.CardImpl;
import com.common.deck.Deck;
import com.common.player.BasePlayer;
import telegram.GameLogicToBot;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public abstract class BaseGameLogic implements GameLogic {

    protected Deck deck;
    protected final BasePlayer[] players;
    protected int currentPlayer;
    protected Scanner scanner = new Scanner(System.in);
    protected GameLogicToBot input;

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
            player.takeHand(createHand(count));
        }
    }

    /**
     * Используется для отправки сообщения пользователю
     * @param message содержание одного сообщения
     */
    protected void sendToUser(String[] message, String playerName, boolean changeKeyboard) {
        switch (Main.type){
            case IN_CONSOLE -> {
                for (String msg : message) {
                    System.out.println(msg);
                }
                System.out.println();
            }
            case IN_TELEGRAM -> {
                if(changeKeyboard){
                    input.sendOutputToUser(playerName,message,String.join("\n",message),true);
                }
                else {
                    input.sendOutputToUser(playerName, new String[0], String.join("\n", message), true);
                }
            }
        }
    }

    protected void sendToAll(String[] message){
        Set<String> set = new HashSet<>();
        for (BasePlayer player:players) {
            set.add(player.name);
        }
        input.sendOutputToAllUsers(set,new String[0],String.join("\n",message));
    }

    /**
     * Получить информацию от пользователя
     * @return возвращает сообщение от пользователя
     */
    protected String getFromUser(){
        switch (Main.type) {
            case IN_CONSOLE -> {
                return scanner.nextLine();
            }
            case IN_TELEGRAM -> {
                return input.getInputToGameLogic();
            }
            default -> throw new RuntimeException();
        }
    }
}