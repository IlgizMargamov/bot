package com.games.fool;

import com.common.card.CardImpl;
import com.common.player.BasePlayer;

import java.util.ArrayList;

public class FoolPlayer extends BasePlayer {

    public void TakeHand(ArrayList<CardImpl> cards) {
        this.hand = cards;
    }

    public void ShowHand() {
        for (int i = 0; i < this.hand.size(); i++) {
            var a = hand.get(i);
            System.out.println(i + 1 + ". " + a.CardSuit + " " +a.CardRank);
        }
    }

    public void TakeCard(CardImpl card){
        hand.add(card);
    }

    public void RemoveCard(int number){
        hand.remove(number);
    }
}
