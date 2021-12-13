package com.common.gamelogic;

import com.common.card.CardImpl;
import com.common.deck.Deck;
import com.common.player.BasePlayer;

import java.util.ArrayList;

public abstract class BaseGameLogic implements GameLogic {

    protected Deck deck;
    protected BasePlayer[] players;
    protected int currentPlayer;

    public BaseGameLogic(BasePlayer[] players, Deck deck) {
        this.deck = deck;
        this.players = players;
        currentPlayer = 0;
    }


    protected abstract int defineFirstPlayer();
    protected abstract boolean checkMoveCorrectness(CardImpl card);
    protected abstract boolean defineWinner();

    protected void movePlayerOn(int count) {
        currentPlayer += count;
        currentPlayer %= players.length;
    }

    protected ArrayList<CardImpl> createHand(int count) {
        ArrayList<CardImpl> hand = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            hand.add(deck.giveNext());
        }
        return hand;
    }

    protected void giveCardToPlayers(int count) {
        for (BasePlayer player : players) {
            player.TakeHand(createHand(count));
        }
    }
}
