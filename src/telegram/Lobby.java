package telegram;

import com.common.deck.Deck;
import com.common.deck.DeckType;
import com.common.gamelogic.BaseGameLogic;
import com.common.player.BasePlayer;
import com.games.fool.FoolLogic;
import com.games.pharaoh.PharaohLogic;

import java.util.*;

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

    private final String createCommand = "/create";
    private final String pinCommand = "/pin";
    private final String helpCommand = "/help";
    private final String showPlayersCommand = "/show_players";
    private final String showGameInfoCommand = "/show_game";
    private final String startGameCommand = "/start_game";
    private final String establishDeckType = "/establish_deck_type";
    String[] availableCommands = {helpCommand, pinCommand, showPlayersCommand, showGameInfoCommand, establishDeckType, startGameCommand};
    String smallDeck = DeckType.SMALL.toString().toUpperCase();
    String mediumDeck = DeckType.MEDIUM.toString().toUpperCase();
    String bigDeck = DeckType.BIG.toString().toUpperCase();
    String[] deckTypes = {smallDeck, mediumDeck, bigDeck};
    private String[] availableCommandsInGame;

    public Lobby(String creator, String chatId, String pin, List<BasePlayer> playersList, GameLogicToBot gameLogicToBot, Game wishedGame) {
        m_creator = creator;
        m_pin = pin;
        m_playerList = playersList; // creator already there
        m_gameLogicToBot = gameLogicToBot;
        m_game = wishedGame;
        m_playerNameToChatId = new HashMap<>();
        m_playersMessages = new LinkedList<>();
        m_playerNameToChatId.put(creator, chatId);
        m_gameLogicToBot.sendOutputToUser(creator,availableCommands, "Invite your friends using your pin: " + m_pin, true);
    }

    @Override
    public void run() {
        while (true) {
            while (!m_playersMessages.isEmpty()) {
                Message message = m_playersMessages.pollFirst();
                if (!m_gameStarted) {
                    if (message.m_message.startsWith(createCommand)) {
                        m_gameLogicToBot.sendOutputToUser(message.m_playerName,
                                availableCommands,
                                "You have entered the @" + m_creator + " lobby.\nUse these buttons to navigate",
                                true);
                        continue; // to skip first message to lobby for each player
                    }
                    if (isEquals(message, pinCommand))
                        m_gameLogicToBot.sendOutputToUser(message.m_playerName,
                                availableCommands,
                                "This lobby pin: " + m_pin,
                                true);
                    if (isEquals(message, showPlayersCommand))
                        m_gameLogicToBot.sendOutputToUser(message.m_playerName,
                                availableCommands,
                                "Current players in lobby: " + getPlayersInLobby(),
                                true);
                    if (isEquals(message, showGameInfoCommand))
                        m_gameLogicToBot.sendOutputToUser(message.m_playerName, availableCommands, getGameInfo(), true);
                    if (isEquals(message, establishDeckType)) {
                        m_gameLogicToBot.sendOutputToUser(
                                message.m_playerName,
                                deckTypes,
                                "Choose type of deck you want to play",
                                true);
                    }
                    for (String deckType : deckTypes) {
                        if (isEquals(message, deckType)) {
                            m_deckType = DeckType.getDeckType(deckType);
                            m_gameLogicToBot.sendOutputToUser(message.m_playerName, availableCommands,
                                    "You have established deck size to be " + m_deckType, true);
                            break;
                        }
                    }

                    if (isEquals(message, startGameCommand)) {
                        Set<String> players = m_playerNameToChatId.keySet();
                        for (String playerName : players) {
                            m_playerList.add(new BasePlayer(playerName));
                        }
                        switch (m_game) {
                            case FOOL -> {
                                m_gameLogic = new FoolLogic(getBasePlayerArray(m_playerList), new Deck(m_deckType), m_gameLogicToBot);
                                m_gameLogic.startGame();
                            }
                            case PHARAOH -> {
                                m_gameLogic = new PharaohLogic(getBasePlayerArray(m_playerList), new Deck(m_deckType), m_gameLogicToBot);
                                m_gameLogic.startGame();
                            }
                            default -> throw new IllegalStateException();
                        }
                        m_gameStarted = true;
                        m_gameLogicToBot.sendOutputToAllUsers(m_playerNameToChatId.keySet(), availableCommandsInGame, "Game has started");
                    }
                } else {
                    m_gameLogicToBot.sendOutputToUser(message.m_playerName, new String[]{"You are playing"}, message.m_message, true);
                }
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private BasePlayer[] getBasePlayerArray(List<BasePlayer> playerList) {
        BasePlayer[] players=new BasePlayer[playerList.size()];
        for (int i=0; i< playerList.size(); i++){
            players[i]=playerList.get(i);
        }

        return players;
    }

    private boolean isEquals(Message message, String command) {
        return message.m_message.equals(command);
    }

    private String getGameInfo() {
        String result = String.format("Lobby creator: %s\n" +
                "Pin for lobby %s\n" +
                "Players: " + getPlayersInLobby() +
                "Game to play: %s", m_creator, m_pin, m_game);

        return result;
    }

    private String getPlayersInLobby() {
        String players = "{\n";
        for (String playerName : m_playerNameToChatId.keySet()) {
            players += "@"+playerName + "\n";
        }
        players+="}\n";

        return players;
    }
}
