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

/*
        System.out.println("Type a size of deck");
        Scanner scanner = new Scanner(System.in);
        String deckTypeString = scanner.nextLine().split(" ")[0];
        DeckType deckType = getDeckType(deckTypeString);
        Deck deck = new Deck(deckType);

        System.out.println("Fool of Pharaoh?");
        String logic = scanner.nextLine();
        BasePlayer player1 = new BasePlayer("player1");
        BasePlayer player2 = new BasePlayer("player2");
        BasePlayer player3 = new BasePlayer("player3");
        BasePlayer[] players = new BasePlayer[]{player1, player2, player3};
        BaseGameLogic gameLogic;
        do {
            gameLogic = LogicFactory.getGameLogic(logic, deck, players);
        } while (gameLogic == null); // if null try again
        gameLogic.startGame();
*/
//        Bot bot = new Bot(gameLogic);
//
//        bot.start();
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