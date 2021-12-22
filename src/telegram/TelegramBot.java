package telegram;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.*;

import static com.common.gamelogic.AnswerToPlayer.*;

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
    private final String[] standardCommands = {startCommand, helpCommand, createLobbyFoolCommand, createLobbyPharaohCommand, joinGameCommand};

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
            String pinPrefix = "#";
            if (messageFromInput.startsWith(pinPrefix)) {
                tryFindLobbyWithGivenPin(messageFromInput, chatId, currentUser);
            } else {
                if (!lobbies.isEmpty()) {
                    for (Lobby lobby : lobbies) {
                        if (lobby.m_playerNameToChatId.containsKey(currentUser)) {
                            lobby.m_playersMessages.add(new Message(currentUser, messageFromInput));
                            return;
                        }
                    }
                } operateWithUserFirstly(messageFromInput, currentUser);
            }
        }

    }

    public void killLobby(String pin){
        for (Lobby lobby : lobbies){
            if (Objects.equals(lobby.m_pin, pin)){
                sendOutputToAllUsers(lobby.m_playerNameToChatId.keySet(), standardCommands, LOBBY_DELETED.getMsg());
                lobbies.remove(lobby);
            }
        }
    }

    private void tryFindLobbyWithGivenPin(String pin, String chatId, String currentUser) {
        boolean isSuccessful = false;
        String friendName = "";
        for (Lobby lobby : lobbies) {
            if (lobby.m_pin.equals(pin)) {
                lobby.m_playerNameToChatId.put(currentUser, chatId);
                isSuccessful = true;
                friendName = lobby.m_creator;
                sendOutputToAllUsers(lobby.m_playerNameToChatId.keySet(), currentAvailableCommands,
                        AT.getMsg()+currentUser+HAVE_ENTERED.getMsg()+AT.getMsg()+friendName+ LOBBY.getMsg());
                break;
            }
        }
        if (!isSuccessful)
            sendOutputToUser(currentUser,
                    currentAvailableCommands,
                     TRY_ASK_AGAIN.getMsg() + pin + OR_CREATE_YOUR_OWN.getMsg(),
                    true );
    }

    public void sendSticker(String cardName, String ownerName){
        //SendMessage.builder().
    }

    private void operateWithUserFirstly(String messageFromInput, String currentUser) {
        switch (messageFromInput) {
            case startCommand -> sendOutputToUser(currentUser,
                    standardCommands,
                    HERE_AVAILABLE_COMMANDS.getMsg(), true);
            case helpCommand -> sendOutputToUser(currentUser,
                    new String[]{createLobbyFoolCommand, createLobbyPharaohCommand, joinGameCommand},
                    HERE_AVAILABLE_COMMANDS.getMsg(), true);
            case createLobbyFoolCommand -> createLobby(currentUser, Game.FOOL);
            case createLobbyPharaohCommand -> createLobby(currentUser, Game.PHARAOH);
            case joinGameCommand -> {
                sendOutputToUser(currentUser,
                        new String[]{createLobbyFoolCommand, createLobbyPharaohCommand, joinGameCommand},
                        PLEASE_ENTER_PIN.getMsg()+OR_CREATE_YOUR_OWN.getMsg(), true);
            }
        }
    }

    // extract in a class?
    private void createLobby(String currentUser, Game gameLogic) {
        Lobby lobby = LobbyCreator.getLobby(currentUser, playerNameToChatId.get(currentUser), gameLogic, this);
        startLobbyThread(lobby);
        lobbies.add(lobby);
    }

    private void startLobbyThread(Lobby lobby) {
        Thread lobbyThread = new Thread(lobby);
        lobbyThread.start();
    }

    /**
     * Sends output from gameLogic to user through GameLogicToBot
     *
     * @param playerName        player to send to
     * @param availableCommands how player may react
     * @param text              what to send to player
     * @param commandsInRows    makes each command a row if true
     */
    public void sendOutputToUser(String playerName, String[] availableCommands, String text, boolean commandsInRows) {
        currentAvailableCommands = availableCommands; // to know possible answers
        String chatId = playerNameToChatId.get(playerName); // find player's chatId by playerName
        SendMessage message = SendMessage
                .builder()
                .text(text)
                .chatId(chatId)
                .build();
        if (availableCommands.length !=0) {
            ReplyKeyboardMarkup replyKeyboardMarkup = getReplyKeyboardMarkup(availableCommands, commandsInRows);
            message.setReplyMarkup(replyKeyboardMarkup);
        }

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void sendOutputToAllUsers(Set<String> players, String[] availableCommands, String text){
        for (String playerName : players){
            sendOutputToUser(playerName, availableCommands, text, true);
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
        return SendMessage
                .builder()
                .chatId(String.valueOf(chatId))
                .text(message)
                .build();
    }
}
