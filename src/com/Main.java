package com;

import com.common.deck.DeckType;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import telegram.TelegramBot;

public class Main {
    public static void main(String[] args) {
        // Instantiate Telegram Bots API
        TelegramBotsApi botsApi;
        TelegramBot bot = new TelegramBot();
        try {
            botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(bot);
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
        /*FoolPlayer player1 = new FoolPlayer("player1");
        FoolPlayer player2 = new FoolPlayer("player2");
        FoolPlayer player3 = new FoolPlayer("player3");
        var players = new FoolPlayer[]{player1,player2,player3};
        FoolLogic game = new FoolLogic(players,new Deck(DeckType.MEDIUM));
        game.startGame();
*/
    }

    private static DeckType getDeckType(String deckTypeString) {
        int deckTypeInteger = Integer.parseInt(deckTypeString);

        return switch (deckTypeInteger) {
            case 24 -> DeckType.SMALL;
            case 36 -> DeckType.MEDIUM;
            case 52 -> DeckType.BIG;
            default -> throw new IllegalStateException("Unexpected value: " + deckTypeInteger);
        };
    }
}
