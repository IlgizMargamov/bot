package telegram;

import com.common.player.BasePlayer;

import java.util.ArrayList;
import java.util.Random;

public class LobbyCreator {
    public static Lobby getLobby(String currentUser, String chatId, Game gameLogic, TelegramBot telegramBot) {
        String pin = getPin();
        ArrayList<BasePlayer> players = new ArrayList<>();
        Lobby lobby = new Lobby(currentUser, chatId, pin, players, new GameLogicToBot(telegramBot), gameLogic);
        return lobby;
    }

    private static String getPin() {
        Random random = new Random();
        int bound = 100000;
        String pin = "#" + Integer.toHexString(random.nextInt(bound));
        return pin;
    }
}
