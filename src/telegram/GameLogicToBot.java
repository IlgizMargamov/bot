package telegram;

import org.telegram.telegrambots.meta.api.objects.InputFile;

import java.util.Set;

public class GameLogicToBot {
    private final TelegramBot m_telegramBot;

    private String m_inputToGameLogic;
    private String m_currentPlayer;
    private String[] m_availableCommands;

    /**
     * Initialize class object
     * @param telegramBot bot with which will operate
     */
    public GameLogicToBot(TelegramBot telegramBot) {
        m_telegramBot = telegramBot;
    }

    /**
     * Communication with game logic
     * @return what to send to game logic
     */
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

    /**
     * Set message to be sent to game logic
     * @param m_message message to set
     */
    public void setInputMessage(String m_message) {
        m_inputToGameLogic = m_message;
    }

    /**
     * Kill specified lobby
     * @param pin how to identify lobby to kill
     */
    public void killLobby(String pin){ m_telegramBot.killLobby(pin);}

    /**
     * Get current player from game logic to not await messages from players which wait for their turn
     * @return player to await
     */
    public String getCurrentPlayer(){ return m_currentPlayer;}

    /**
     * Available to player commands
     * @return available commands
     */
    public String[] getAvailableCommands(){return m_availableCommands;}

    /**
     * Send photo of a card to user
     * @param players to whom send the photo
     * @param file the card
     * @param ownerName who throws the card
     */
    public void sendPhoto(Set<String> players, InputFile file, String ownerName){
        m_telegramBot.sendPhoto(players, file, ownerName);
    }
}
