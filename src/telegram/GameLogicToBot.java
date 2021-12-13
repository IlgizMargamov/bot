package telegram;

public class GameLogicToBot {
    private TelegramBot m_telegramBot;

    public GameLogicToBot(TelegramBot telegramBot){
        m_telegramBot=telegramBot;
    }

    public String getInputToGameLogic(){
        return m_telegramBot.getInputToGameLogic();
    }

    public void sendOutputToUser(String playerName, String[] availableCommands){
        m_telegramBot.sendOutputToUser(playerName, availableCommands);
    }
}
