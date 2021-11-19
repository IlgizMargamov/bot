package com.common.gamelogic;

import com.common.player.Player;

public interface GameLogic {
    Player defineFirstPlayer(Player[] players);
    void checkMoveCorrectness();
    void defineCardCountPerPlayer();
    void defineWinner();
}
