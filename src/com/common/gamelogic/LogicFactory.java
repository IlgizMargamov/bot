package com.common.gamelogic;

import com.common.deck.Deck;
import com.games.blackjack.BlackJackLogic;
import com.games.fool.FoolPlayer;
import com.games.fool.foolLogic;
//TODO: Remake Factory
public class LogicFactory {
    public static BaseGameLogic getGameLogic(String gameLogic, Deck deck) {
        String game = gameLogic.toUpperCase();

        if (game.startsWith("F")){
            return new foolLogic(new FoolPlayer[2],deck);
        }

        if (game.startsWith("B")){
            return new BlackJackLogic(deck);
        }

        return null;
    }
}
