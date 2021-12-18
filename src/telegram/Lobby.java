package telegram;

import com.common.player.BasePlayer;

import java.util.*;

public class Lobby implements Runnable {
    public String m_creator;
    public String m_pin;
    public List<BasePlayer> m_playerList;
    public GameLogicToBot m_gameLogicToBot;
    public Game m_gameLogic;
    public Map<String, String> m_playerNameToChatId;
    public LinkedList<Message> m_playersMessages;

    public Lobby(String creator, String chatId, String pin, List<BasePlayer> playersList, GameLogicToBot gameLogicToBot, Game gameLogic) {
        m_creator = creator;
        m_pin = pin;
        m_playerList = playersList;
        m_gameLogicToBot = gameLogicToBot;
        m_gameLogic = gameLogic;
        m_playerNameToChatId = new HashMap<>();
        m_playersMessages = new LinkedList<>();
        m_playersMessages.add(new Message(creator, "Invite your friends using your pin: "+m_pin));
        m_playerNameToChatId.put(creator, chatId);
    }

    @Override
    public void run() {
        while (true) {
            while (!m_playersMessages.isEmpty()) {
                Message message = m_playersMessages.pollFirst();
                if (message.m_message.startsWith("/create")) continue;
                m_gameLogicToBot.sendOutputToUser(message.m_playerName, new String[]{message.m_message}, true);
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
