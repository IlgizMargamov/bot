package com.common.gamelogic;

import com.common.deck.Deck;
import com.games.blackjack.BlackJackLogic;
import com.games.fool.FoolLogic;

public class LogicFactory {
    public static BaseGameLogic getGameLogic(String gameLogic, Deck deck) {
        String game = gameLogic.toUpperCase();

        if (game.startsWith("F")){
            return new FoolLogic(deck);
        }

        if (game.startsWith("B")){
            return new BlackJackLogic(deck);
        }

        return null;
    }
}
