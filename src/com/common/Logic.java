package com.common;

import com.blackjack.BlackJackLogic;
import com.fool.FoolLogic;

public class Logic {
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
