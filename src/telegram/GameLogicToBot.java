package telegram;

import java.util.Set;

public class GameLogicToBot {
    private final TelegramBot m_telegramBot;

    private String m_inputToGameLogic;

    public GameLogicToBot(TelegramBot telegramBot) {
        m_telegramBot = telegramBot;
    }

    public String getM_inputToGameLogic() {
        return m_inputToGameLogic;
    }

    /**
     * Sends output from gameLogic to user
     *
     * @param playerName        player to send to
     * @param availableCommands how player may react
     * @param text              what to send to player
     * @param commandsInRows    makes each command a row if true
     */
    public void sendOutputToUser(String playerName, String[] availableCommands, String text, boolean commandsInRows) {
        m_telegramBot.sendOutputToUser(playerName, availableCommands, text, commandsInRows);
    }

    /**
     * Sends output from gameLogic to all users
     *
     * @param playersName       players which need to be addressed
     * @param availableCommands how player may react
     * @param text              how player may react
     */
    public void sendOutputToAllUsers(Set<String> playersName, String[] availableCommands, String text) {
        m_telegramBot.sendOutputToAllUsers(playersName, availableCommands, text);
    }

    public void setInputMessage(String m_message) {
        m_inputToGameLogic = m_message;
    }
}
