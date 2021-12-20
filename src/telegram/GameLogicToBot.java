package telegram;

import java.util.Set;

public class GameLogicToBot {
    private final TelegramBot m_telegramBot;

    private String m_inputToGameLogic;
    private String m_currentPlayer;
    private String[] m_availableCommands;

    public GameLogicToBot(TelegramBot telegramBot) {
        m_telegramBot = telegramBot;
    }

    public String getInputToGameLogic() {
        while (m_inputToGameLogic == null){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        String tmp=m_inputToGameLogic;
        m_inputToGameLogic=null;
        return tmp;
    }

    /**
     * Sends output from gameLogic to user
     *
     * @param playerName        player to send to
     * @param availableCommands how player may react
     * @param text              what to send to player
     * @param commandsInRows    makes each command a row if true
     * @param fromGame true for changing expectedPlayer
     */
    public void sendOutputToUser(String playerName, String[] availableCommands, String text, boolean commandsInRows, boolean fromGame) {
        if (fromGame) m_currentPlayer=playerName;
        m_availableCommands=availableCommands;
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

    public void setInputMessage(char m_message) {
        m_inputToGameLogic = String.valueOf(m_message);
    }

    public String getCurrentPlayer(){ return m_currentPlayer;}
    public String[] getAvailableCommands(){return m_availableCommands;}
}
