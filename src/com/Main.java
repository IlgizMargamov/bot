package com;

import com.common.deck.Deck;
import com.common.deck.DeckType;
import com.common.gamelogic.BaseGameLogic;
import com.common.gamelogic.LogicFactory;

import java.util.Scanner;


public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        String deckTypeString=scanner.nextLine().split(" ")[0];
        DeckType deckType=getDeckType(deckTypeString);
        Deck deck=new Deck(deckType);

        String logic = scanner.nextLine();
        BaseGameLogic gameLogic = LogicFactory.getGameLogic(logic, deck); // if null try again

        Bot bot = new Bot(gameLogic);

        bot.start();
    }

    private static DeckType getDeckType(String deckTypeString){
        int deckTypeInteger=Integer.parseInt(deckTypeString);

        switch (deckTypeInteger){
            case 24:
                return DeckType.SMALL;
            case 36:
                return DeckType.MEDIUM;
            case 52:
                return DeckType.BIG;
            default:
                throw new IllegalStateException("Unexpected value: " + deckTypeInteger);
        }
    }
}
