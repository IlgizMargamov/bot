package com.games.fool;

import com.common.card.Card;
import com.common.player.BasePlayer;

public class FoolPlayer extends BasePlayer {

    public void TakeHand(Card[] cards){
        this.hand = cards;
    }
}
