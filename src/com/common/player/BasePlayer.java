package com.common.player;

import com.common.card.CardImpl;

import java.util.ArrayList;

/**
 * Класс игрока
 */
public class BasePlayer implements Player {
    /**
     * Рука игрока
     */
    public ArrayList<CardImpl> hand;
    /**
     * Имя игрока (тег в тг)
     */
    public String name;

    /**
     * Создание игрока по имени
     * @param name как назвать игрока
     */
    public BasePlayer(String name){
        this.name = name;
    }

    /**
     * Инициализация объекта
     */
    public BasePlayer(){}

    /**
     * Получение кард в руку
     * @param cards карты в руку
     */
    public void takeHand(ArrayList<CardImpl> cards) {
        this.hand = cards;
    }

    /**
     * Показывает, что на  руке
     * @return рука игрока
     */
    public ArrayList<String> showHand() {
        ArrayList<String> array = new ArrayList<>();
        for (int i = 0; i < this.hand.size(); i++) {
            var a = hand.get(i);
            array.add(i + 1 + ". " + a.CardSuit + " " +a.CardRank);
        }
        array.add("");
        return array;
    }

    /**
     * Игрок берет карту
     * @param card карта, которую взять
     */
    public void takeCard(CardImpl card){
        hand.add(card);
    }

    /**
     * Удаление карты
     * @param number индекс карты для удаления в руке
     */
    public void removeCard(int number){
        hand.remove(number);
    }

    /**
     * Выдача последней карты
     * @return последняя карта
     */
    public CardImpl giveLastCard(){
        return hand.remove(hand.size()-1);
    }
}