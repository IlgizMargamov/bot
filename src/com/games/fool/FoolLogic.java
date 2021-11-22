package com.games.fool;

import com.common.card.Card;
import com.common.deck.Deck;
import com.common.deck.DeckType;
import com.common.gamelogic.BaseGameLogic;

public class FoolLogic extends BaseGameLogic {

    private boolean GameNotEnd = true;
    Deck deck = new Deck(DeckType.BIG);
    int currentPlayer = 0;
    FoolPlayer[] players;

    public FoolLogic(FoolPlayer[] players) {
        this.players = players;
        GiveCardToPlayer();
        while (GameNotEnd){
            MakeTurn();
        }
        //new Deck(DeckType.BIG)
    }

    private void GiveCardToPlayer(){
        for (int i = 0; i <players.length; i++) {
            players[i].TakeHand(CreateHand(6));
        }
    }

    private Card[] CreateHand(int count){
        Card[] hand = new Card[count];
        for (int i = 0; i <count; i++) {
            hand[i] = deck.GiveNext();
        }
        return  hand;
    }

    private void MakeTurn(){
        System.out.println("Make your turn(type number of card in hand)");

    }
}
