package com.common.gamelogic;

import com.games.blackjack.BlackJackLogic;
import com.games.fool.FoolLogic;
//TODO: Remake Factory
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
