package com.common.table;

public interface Table {
    void giveCardsToPlayers();
    void estimateFirstPlayer();
    void letPlayerHaveHisTurn();
    void checkTurnCorrectness();
    void letPlayersMakeTurn();
    void endTurn();
}
