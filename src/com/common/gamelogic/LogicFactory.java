package com.common.gamelogic;

import com.blackjack.BlackJackLogic;
import com.common.gamelogic.BaseGameLogic;
import com.fool.FoolLogic;

public class LogicFactory {
    public static BaseGameLogic getGameLogic(String gameLogic) {
        String game = gameLogic.toUpperCase();
        if (game.startsWith("F")){
            return new FoolLogic();
        }

        if (game.startsWith("B")){
            return new BlackJackLogic();
        }

        return null;
    }
}
