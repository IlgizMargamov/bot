package com;

import com.common.deck.Deck;
import com.common.deck.DeckType;
import com.games.fool.FoolPlayer;
import com.games.fool.foolLogic;


public class Main {
    public static void main(String[] args) {
//        Scanner scanner = new Scanner(System.in);
//
//        String deckTypeString=scanner.nextLine().split(" ")[0];
//        DeckType deckType=getDeckType(deckTypeString);
//        Deck deck=new Deck(deckType);
//
//        String logic = scanner.nextLine();
//        BaseGameLogic gameLogic = LogicFactory.getGameLogic(logic, deck); // if null try again
//
//        Bot bot = new Bot(gameLogic);
//
//        bot.start();

        FoolPlayer player1 = new FoolPlayer();
        FoolPlayer player2 = new FoolPlayer();
        var players = new FoolPlayer[]{player1,player2};
        foolLogic game = new foolLogic(players,new Deck(DeckType.MEDIUM));
        game.StartGame();
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
