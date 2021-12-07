package com.common.gamelogic;

import com.common.deck.Deck;
import com.common.player.BasePlayer;
import com.common.player.Player;

public abstract class BaseGameLogic implements GameLogic {

    protected Deck deck;
    BasePlayer[] players;

    public BaseGameLogic(BasePlayer[] players, Deck deck) {
        this.deck = deck;
        this.players = players;
    }

    protected BaseGameLogic() {
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
