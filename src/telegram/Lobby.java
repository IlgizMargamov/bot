package telegram;

import com.common.deck.Deck;
import com.common.deck.DeckType;
import com.common.gamelogic.BaseGameLogic;
import com.common.player.BasePlayer;
import com.games.fool.FoolLogic;
import com.games.pharaoh.PharaohLogic;

import java.util.*;


//TODO: Парсить команды до точки.
public class Lobby implements Runnable {
    public String m_creator;
    public String m_pin;
    public List<BasePlayer> m_playerList;
    public GameLogicToBot m_gameLogicToBot;
    public Game m_game;
    public Map<String, String> m_playerNameToChatId;
    public LinkedList<Message> m_playersMessages;

    private BaseGameLogic m_gameLogic;
    private DeckType m_deckType = DeckType.MEDIUM;
    private boolean m_gameStarted;
    private String m_expectedPlayer;
    private String[] m_availableCommandsInGame;
    private Thread m_gameThread;

    private final String createCommand = "/create";
    private final String pinCommand = "/pin";
    private final String helpCommand = "/help";
    private final String showPlayersCommand = "/show_players";
    private final String showGameInfoCommand = "/show_game";
    private final String startGameCommand = "/start_game";
    private final String establishDeckType = "/establish_deck_type";
    private final String whatIsTrumpCommand = "/what_is_trump";
    private final String whatOnTheTableCommand = "/what_on_table";
    private final String cardsInDeckCommand = "/cards_in_deck";
    private final String leaveLobbyCommand = "/leave_lobby";
    private final String quitGameCommand = "/quit_game";
    private final String[] m_defaultCommands = {whatIsTrumpCommand, whatOnTheTableCommand, cardsInDeckCommand, quitGameCommand};
    private final String smallDeck = DeckType.SMALL.toString().toUpperCase();
    private final String mediumDeck = DeckType.MEDIUM.toString().toUpperCase();
    private final String bigDeck = DeckType.BIG.toString().toUpperCase();
    private final String[] deckTypes = {smallDeck, mediumDeck, bigDeck};
    private String[] availableCommands = {helpCommand, pinCommand, showPlayersCommand, showGameInfoCommand, establishDeckType, startGameCommand, leaveLobbyCommand};

    public Lobby(String creator, String chatId, String pin, List<BasePlayer> playersList, GameLogicToBot gameLogicToBot, Game wishedGame) {
        m_creator = creator;
        m_pin = pin;
        m_playerList = playersList; // creator already there
        m_gameLogicToBot = gameLogicToBot;
        m_game = wishedGame;
        m_playerNameToChatId = new HashMap<>();
        m_playersMessages = new LinkedList<>();
        m_playerNameToChatId.put(creator, chatId);
        sendOutputToUser(creator, availableCommands, "Invite your friends using your pin: " + m_pin, true);
    }

    @Override
    public void run() {
        while (true) {
            while (!m_playersMessages.isEmpty()) {
                Message message = m_playersMessages.pollFirst();
                if (!m_gameStarted) {
                    if (message.m_message.startsWith(createCommand)) {
                        sendOutputToUser(message.m_playerName,
                                availableCommands,
                                "You have entered the @" + m_creator + " lobby.\nUse these buttons to navigate",
                                true);
                        continue; // to skip first message to lobby for each player
                    }
                    if (isEquals(message, leaveLobbyCommand)) {
                        sendOutputToUser(message.m_playerName, availableCommands, "You leaved from " + m_creator + " lobby", true);
                        m_playerNameToChatId.remove(message.m_playerName);
                    }
                    if (isEquals(message, pinCommand))
                        sendOutputToUser(message.m_playerName,
                                availableCommands,
                                "This lobby pin: " + m_pin,
                                true);
                    if (isEquals(message, showPlayersCommand))
                        sendOutputToUser(message.m_playerName,
                                availableCommands,
                                "Current players in lobby: " + getPlayersInLobby(),
                                true);
                    if (isEquals(message, showGameInfoCommand))
                        sendOutputToUser(message.m_playerName, availableCommands, getGameInfo(), true);
                    if (isEquals(message, establishDeckType)) {
                        sendOutputToUser(
                                message.m_playerName,
                                deckTypes,
                                "Choose type of deck you want to play",
                                true);
                    }
                    for (String deckType : deckTypes) {
                        if (isEquals(message, deckType)) {
                            m_deckType = DeckType.getDeckType(deckType);
                            m_gameLogicToBot.sendOutputToAllUsers(m_playerNameToChatId.keySet(),
                                    availableCommands, "Deck type has been set to: " + m_deckType + " by @" + message.m_playerName);
                            break;
                        }
                    }

                    if (isEquals(message, startGameCommand)) {
                        Set<String> players = m_playerNameToChatId.keySet();
                        for (String playerName : players) {
                            m_playerList.add(new BasePlayer(playerName));
                        }
                        switch (m_game) {
                            case FOOL -> m_gameLogic = new FoolLogic(getBasePlayerArray(m_playerList), new Deck(m_deckType), m_gameLogicToBot);
                            case PHARAOH -> m_gameLogic = new PharaohLogic(getBasePlayerArray(m_playerList), new Deck(m_deckType), m_gameLogicToBot);
                            default -> throw new IllegalStateException();
                        }
                        m_gameThread = new Thread(m_gameLogic);
                        m_gameStarted = true;
                        m_gameLogicToBot.sendOutputToAllUsers(m_playerNameToChatId.keySet(), m_availableCommandsInGame, "Game has started");
                        try {
                            Thread.sleep(50);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        m_gameThread.start();

                    }
                } else { // in-game logic
                    if (message.m_message.startsWith("/start")) continue;
                    m_expectedPlayer = m_gameLogicToBot.getCurrentPlayer();
                    m_availableCommandsInGame = m_gameLogicToBot.getAvailableCommands();
                    if (isEquals(message, quitGameCommand)) {
                        m_gameStarted=false;
                        sendOutputToUser(message.m_playerName, availableCommands, "You left "+m_creator+ "lobby", true);
                        m_gameLogicToBot.killLobby(m_pin);
                        return;
                    }
                    if (message.m_playerName.equals(m_expectedPlayer)) { // message from the awaited player
                        //sendOutputToUser(message.m_playerName, new String[]{"You are playing"}, message.m_message, true);
                        boolean correctCommand = false;
                        for (String availableCommand : m_availableCommandsInGame) {
                            if (availableCommand.equals(message.m_message)) {
                                sendInputToGameLogic(message.m_message.charAt(0));
                                correctCommand = true;
                                break;
                            }
                        }
                        if (!correctCommand)
                            sendOutputToUser(message.m_playerName,
                                    m_defaultCommands,
                                    "Wrong command.\nTry again,please",
                                    true);
                    } else {
                        sendOutputToUser(message.m_playerName,
                                m_defaultCommands,
                                "Not your turn yet.\nPlease, wait",
                                true);
                    }
                }
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void sendInputToGameLogic(char m_message) {
        m_gameLogicToBot.setInputMessage(m_message);
    }

    private void sendOutputToUser(String playerName, String[] availableCommands, String text, boolean commandsInRows) {
        m_availableCommandsInGame = availableCommands;
        m_gameLogicToBot.sendOutputToUser(playerName, availableCommands, text, commandsInRows, false);
    }

    private BasePlayer[] getBasePlayerArray(List<BasePlayer> playerList) {
        BasePlayer[] players = new BasePlayer[playerList.size()];
        for (int i = 0; i < playerList.size(); i++) {
            players[i] = playerList.get(i);
        }

        return players;
    }

    private boolean isEquals(Message message, String command) {
        return message.m_message.equals(command);
    }

    private String getGameInfo() {
        return String.format("Lobby creator: %s\n" +
                "Pin for lobby %s\n" +
                "Players: " + getPlayersInLobby() +
                "Game to play: %s\n" +
                "Deck type: %s", m_creator, m_pin, m_game, m_deckType);
    }

    private String getPlayersInLobby() {
        StringBuilder players = new StringBuilder("{\n");
        for (String playerName : m_playerNameToChatId.keySet()) {
            players.append("@").append(playerName).append("\n");
        }
        players.append("}\n");

        return players.toString();
    }
}
