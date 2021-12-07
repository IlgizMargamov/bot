package com.common.gamelogic;

import com.common.deck.Deck;
import com.games.fool.FoolPlayer;
import com.games.fool.FoolLogic;
import com.games.pharaoh.PharaohLogic;
import com.games.pharaoh.PharaohPlayer;

//TODO: Remake Factory
public class LogicFactory {
    public static BaseGameLogic getGameLogic(String gameLogic, Deck deck) {
        String game = gameLogic.toUpperCase();

        if (game.startsWith("F")){
            return new FoolLogic(new FoolPlayer[2],deck);
        }

        if (game.startsWith("P")){
            return new PharaohLogic(new PharaohPlayer[2], deck);
        }

        return null;
    }
}
