package com;


import com.common.deck.Deck;
import com.common.deck.DeckType;
import com.games.OneHundred.OneHundredLogic;
import com.games.OneHundred.OneHundredPlayer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import telegram.TelegramBot;


public class Main {
    public static void main(String[] args) {
        // Instantiate Telegram Bots API
        TelegramBotsApi botsApi = null;
        try {
            botsApi = new TelegramBotsApi(DefaultBotSession.class);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

        // Register our bot
        try {
            assert botsApi != null;
            botsApi.registerBot(new TelegramBot());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
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
        /*FoolPlayer player1 = new FoolPlayer();
        FoolPlayer player2 = new FoolPlayer();
        var players = new FoolPlayer[]{player1,player2};
        FoolLogic game = new FoolLogic(players,new Deck(DeckType.MEDIUM));
        game.startGame();
*/

        OneHundredPlayer player1 = new OneHundredPlayer();
        OneHundredPlayer player2 = new OneHundredPlayer();
        var players = new OneHundredPlayer[]{player1,player2};
        OneHundredLogic game = new OneHundredLogic(players,new Deck(DeckType.MEDIUM));
        game.StartGame();
    }

    private static DeckType getDeckType(String deckTypeString){
        int deckTypeInteger=Integer.parseInt(deckTypeString);

        return switch (deckTypeInteger) {
            case 24 -> DeckType.SMALL;
            case 36 -> DeckType.MEDIUM;
            case 52 -> DeckType.BIG;
            default -> throw new IllegalStateException("Unexpected value: " + deckTypeInteger);
        };
    }
}
