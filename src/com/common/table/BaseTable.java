package com.common.table;

import com.common.gamelogic.GameLogic;
import com.common.player.Player;

public class BaseTable implements Table {
    private GameLogic baseGameLogic;
    private Player[] players;

    @Override
    public void giveCardsToPlayers() {
        Player firstPlayer=baseGameLogic.defineFirstPlayer(players);
    }

    @Override
    public void estimateFirstPlayer() {

    }

    @Override
    public void letPlayerHaveHisTurn() {

    }

    @Override
    public void checkTurnCorrectness() {

    }

    @Override
    public void letPlayersMakeTurn() {

    }

    @Override
    public void endTurn() {

    }
}
