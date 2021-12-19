package telegram;

public class GameLogicToBot {
    private final TelegramBot m_telegramBot;

    public GameLogicToBot(TelegramBot telegramBot){
        m_telegramBot=telegramBot;
    }

    public String getInputToGameLogic(){
        return m_telegramBot.getInputToGameLogic();
    }

    /**
     * Sends output from gameLogic to user
     * @param playerName player to send to
     * @param availableCommands how player may react
     * @param text what to send to player
     * @param commandsInRows makes each command a row if true
     * */
    public void sendOutputToUser(String playerName, String[] availableCommands, String text, boolean commandsInRows){
        m_telegramBot.sendOutputToUser(playerName, availableCommands, text, commandsInRows);
    }
}
