package telegram;

import com.common.player.BasePlayer;

import java.util.ArrayList;
import java.util.Random;

public class LobbyCreator {
    public static Lobby getLobby(String currentUser, String chatId, Game gameLogic, TelegramBot telegramBot) {
        String pin = getPin();
        ArrayList<BasePlayer> players = getBasePlayers(currentUser);
        Lobby lobby = new Lobby(currentUser, chatId, pin, players, new GameLogicToBot(telegramBot), gameLogic);
        return lobby;
    }

    private static ArrayList<BasePlayer> getBasePlayers(String currentUser) {
        BasePlayer creatorPlayer = new BasePlayer(currentUser);
        ArrayList<BasePlayer> players = new ArrayList<>();
        players.add(creatorPlayer);
        return players;
    }

    private static String getPin() {
        Random random = new Random();
        int bound = 100000;
        String pin = "#" + Integer.toHexString(random.nextInt(bound));
        return pin;
    }
//

}
