package com.common.gamelogic;

import com.common.deck.Deck;
import com.common.player.Player;

public abstract class BaseGameLogic implements GameLogic {

    public BaseGameLogic(Deck deck){

    }

    @Override
    public Player defineFirstPlayer(Player[] players) {

        return null;
    }

    @Override
    public void checkMoveCorrectness() {

    }

    @Override
    public void defineCardCountPerPlayer() {

    }

    @Override
    public void defineWinner() {

    }
}
