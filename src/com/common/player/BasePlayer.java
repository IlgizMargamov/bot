package com.common.player;

import com.common.card.CardImpl;

import java.util.ArrayList;


public class BasePlayer implements Player {
    public ArrayList<CardImpl> hand;
    public String name;

    public BasePlayer(String name){
        this.name = name;
    }
    public BasePlayer(){}

    public void TakeHand(ArrayList<CardImpl> cards) {
        this.hand = cards;
    }

    public ArrayList<String> ShowHand() {
        ArrayList<String> array = new ArrayList<>();
        for (int i = 0; i < this.hand.size(); i++) {
            var a = hand.get(i);
            array.add(i + 1 + ". " + a.CardSuit + " " +a.CardRank);
        }
        array.add("");
        return array;
    }

    public void TakeCard(CardImpl card){
        hand.add(card);
    }

    public void RemoveCard(int number){
        hand.remove(number);
    }

    public CardImpl GiveLastCard(){
        return hand.remove(hand.size()-1);
    }
}