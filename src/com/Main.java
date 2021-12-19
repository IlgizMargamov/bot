package com;

import com.common.deck.Deck;
import com.common.deck.DeckType;
import com.common.gamelogic.BaseGameLogic;
import com.common.gamelogic.LogicFactory;
import com.common.player.BasePlayer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import telegram.TelegramBot;

import java.util.Scanner;

import static com.common.deck.DeckType.getDeckType;

public class Main {

    public static TypeOfCommunication type;
    private static final String chooseHost = "Where you want to start?\n 1. Console.\n 2. Telegram.";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println(chooseHost);
        type = TypeOfCommunication.getType(Integer.parseInt(scanner.nextLine()));
        switch (type) {
            case IN_TELEGRAM -> {
                // Instantiate Telegram Bots API
                TelegramBotsApi botsApi;
                TelegramBot bot = new TelegramBot();
                try {
                    botsApi = new TelegramBotsApi(DefaultBotSession.class);
                    botsApi.registerBot(bot);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
            case IN_CONSOLE -> {

                System.out.println("Type a size of deck");

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
            }
        }
    }
}