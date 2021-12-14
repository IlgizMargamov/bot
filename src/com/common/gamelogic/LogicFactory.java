package com.common.gamelogic;

import com.common.deck.Deck;
import com.common.player.BasePlayer;
import com.games.fool.FoolLogic;
import com.games.pharaoh.PharaohLogic;

//TODO: Remake Factory
public class LogicFactory {
    public static BaseGameLogic getGameLogic(String gameLogic, Deck deck, BasePlayer[] players) {
        String game = gameLogic.toUpperCase();

        if (game.startsWith("F")){
            return new FoolLogic(players,deck);
        }

        if (game.startsWith("P")){
            return new PharaohLogic(players, deck);
        }
        return null;
    }
}