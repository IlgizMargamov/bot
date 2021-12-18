package telegram;

import com.common.player.BasePlayer;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.*;

public class TelegramBot extends TelegramLongPollingBot {
    private final String token = "5008512617:AAGrCuVOt6wfZPqQJzxtBp93sTSEYStl5yg";
    private final String botUsername = "Card Games";

    private Map<String, String> playerNameToChatId = new HashMap<>(); //
    private List<Lobby> lobbies = new ArrayList<>();

    private String[] currentAvailableCommands; // for checking if player answered expectedly
    private final String startCommand = "/start";
    private final String helpCommand = "/help";
    private final String createLobbyPharaohCommand = "/create_lobby_pharaoh";
    private final String createLobbyFoolCommand = "/create_lobby_fool";
    private final String startGame = "/start_game";
    private final String joinGameCommand = "/join_game";

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return token;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageFromInput = update.getMessage().getText();
            String chatId = update.getMessage().getChatId().toString();
            String currentUser = update.getMessage().getChat().getUserName();

            if (!playerNameToChatId.containsKey(currentUser)) playerNameToChatId.put(currentUser, chatId);

            userAndBotFirstMeeting(messageFromInput, currentUser);

            if (messageFromInput.startsWith("#")) {
                tryFindLobbyWithGivenPin(messageFromInput, chatId, currentUser);
            } else {
                for (Lobby lobby : lobbies) {
                    if (lobby.m_playerNameToChatId.containsKey(currentUser)) {
                        lobby.m_playersMessages.add(new Message(currentUser, messageFromInput));
                    }
                }
            }
        }
    }

    private void tryFindLobbyWithGivenPin(String messageFromInput, String chatId, String currentUser) {
        boolean isSuccessful = false;
        String friendName = "";
        for (Lobby lobby : lobbies) {
            if (lobby.m_pin.equals(messageFromInput)) {
                lobby.m_playerNameToChatId.put(currentUser, chatId);
                isSuccessful = true;
                friendName = lobby.m_creator;
                break;
            }
        }
        if (!isSuccessful)
            sendMessageToUser("Try asking your friend the pin once again.\nYou typed: " + messageFromInput + "\nOr create your own lobby", currentUser);
        else sendMessageToUser("You have been successfully added to the @" + friendName + " lobby", currentUser);
    }

    private void userAndBotFirstMeeting(String messageFromInput, String currentUser) {
        switch (messageFromInput) {
            case startCommand -> sendAvailableCommandsToUser(currentUser, new String[]{startCommand, helpCommand, createLobbyFoolCommand, createLobbyPharaohCommand, joinGameCommand}, true);
            case helpCommand -> sendAvailableCommandsToUser(currentUser, new String[]{createLobbyFoolCommand, createLobbyPharaohCommand, joinGameCommand}, true);
            case createLobbyFoolCommand -> createLobby(currentUser, Game.FOOL);
            case createLobbyPharaohCommand -> createLobby(currentUser, Game.PHARAOH);
            case joinGameCommand -> {
                sendAvailableCommandsToUser(currentUser, new String[]{createLobbyFoolCommand, createLobbyPharaohCommand, joinGameCommand}, true);
                sendMessageToUser("Please enter the pin from the game you want to enter", currentUser);
            }
        }
    }

    // extract in a class?
    private void createLobby(String currentUser, Game gameLogic) {
        Lobby lobby = getLobby(currentUser, gameLogic);
        startLobbyThread(lobby);
        lobbies.add(lobby);
    }

    private void startLobbyThread(Lobby lobby) {
        Thread lobbyThread = new Thread(lobby);
        lobbyThread.start();
    }

    private Lobby getLobby(String currentUser, Game gameLogic) {
        String pin = getPin();
        ArrayList<BasePlayer> players = getBasePlayers(currentUser);
        Lobby lobby = new Lobby(currentUser, playerNameToChatId.get(currentUser), pin, players, new GameLogicToBot(this), gameLogic);
        return lobby;
    }

    private ArrayList<BasePlayer> getBasePlayers(String currentUser) {
        BasePlayer creatorPlayer = new BasePlayer(currentUser);
        ArrayList<BasePlayer> players = new ArrayList<>();
        players.add(creatorPlayer);
        return players;
    }

    private String getPin() {
        Random random = new Random();
        int bound = 100000;
        String pin = "#" + Integer.toHexString(random.nextInt(bound));
        return pin;
    }
//

    public String getInputToGameLogic() {
        return null;
    }

    /**
     * Sends output from gameLogic to user through GameLogicToBot
     *
     * @param playerName        player to send to
     * @param availableCommands how player may react
     * @param commandsInRows    makes each command a row if true
     */
    public void sendAvailableCommandsToUser(String playerName, String[] availableCommands, boolean commandsInRows) {
        currentAvailableCommands = availableCommands; // to know possible answers
        String chatId = playerNameToChatId.get(playerName); // find player's chatId by playerName
        SendMessage message = SendMessage
                .builder()
                .text("Use these buttons for your own comfort") // need to send intended message from here just adding another param
                .chatId(chatId)
                .build();

        ReplyKeyboardMarkup replyKeyboardMarkup = getReplyKeyboardMarkup(availableCommands, commandsInRows);
        message.setReplyMarkup(replyKeyboardMarkup);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets ReplyKeyboardMarkup using given commands
     *
     * @param commands       available to user commands
     * @param commandsInRows make each command a row
     * @return ReplyKeyboardMarkup object
     */
    private ReplyKeyboardMarkup getReplyKeyboardMarkup(String[] commands, boolean commandsInRows) {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();

        List<KeyboardRow> keyboardRows = new ArrayList<>();

        if (commandsInRows) {
            for (String command : commands) {
                KeyboardRow row = new KeyboardRow();
                row.add(command);
                keyboardRows.add(row);
            }
        } else {
            KeyboardRow row = new KeyboardRow();
            for (String command : commands) {
                row.add(command);
            }
            keyboardRows.add(row);
        }


        keyboardMarkup.setKeyboard(keyboardRows);
        return keyboardMarkup;
    }

    /**
     * Sends message to user
     *
     * @param message    what to send
     * @param playerName whom to send
     */
    private void sendMessageToUser(String message, String playerName) {
        String chatId = playerNameToChatId.get(playerName);
        SendMessage sendMessage = getSendMessage(message, chatId);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates message to send using targeted chatId and message itself
     *
     * @param message message to send
     * @param chatId  what chat to send to
     * @return SendMessage object
     */
    private SendMessage getSendMessage(String message, String chatId) {
        SendMessage sendMessage = SendMessage
                .builder()
                .chatId(String.valueOf(chatId))
                .text(message)
                .build();
        return sendMessage;
    }
}
