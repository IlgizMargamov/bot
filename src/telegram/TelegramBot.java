package telegram;

import com.common.deck.Deck;
import com.common.deck.DeckType;
import com.games.fool.FoolLogic;
import com.games.fool.FoolPlayer;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.*;

public class TelegramBot extends TelegramLongPollingBot {
    public static String input;
    public static String output;

    public FoolLogic gameLogic;

    private final String token = "5008512617:AAELuxvMo_D0hg1C8pHiRN52NYWhewlHgAw";
    private final String botUsername = "Card Games";

    private Map<String, String> playerNameToChatId;
    private List<Lobby> lobbies=new ArrayList<>();
    private String[] currentAvailableCommands;

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

            String startCommand = "/start";
            String helpCommand = "/help";
            String createLobbyPharaohCommand = "/create_lobby_pharaoh";
            String createLobbyFoolCommand = "/create_lobby_fool";
            String startGame = "/start_game";

            // TODO: check from who comes the request; if not awaited chatId then print them "not your turn yet"

            if (messageFromInput.equals(startCommand)) {
                ReplyKeyboardMarkup keyboardMarkup = getStartReplyKeyboardMarkup();

                SendMessage sendMessage = SendMessage.builder().chatId(chatId).text("Now you can navigate through buttons").build();
                sendMessage.setReplyMarkup(keyboardMarkup);
                try {
                    execute(sendMessage);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            } else if (messageFromInput.equals(helpCommand)) {
                try {
                    execute(SendMessage.builder().chatId(chatId).text("This is gonna help you").build());
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            } else if (messageFromInput.startsWith(createLobbyFoolCommand.substring(0, 10))) {
                String[] strings = messageFromInput.split("_");
                Random random=new Random();
                String pin = "#" + Integer.toHexString(random.nextInt(100000));
                String creator = currentUser;
                if (strings[strings.length - 1].equals("fool")) {
                    playerNameToChatId = new HashMap<>();
                    playerNameToChatId.put(creator, chatId);
                    lobbies.add(new Lobby(creator, pin, playerNameToChatId));
                    try {
                        execute(SendMessage.builder().chatId(chatId).text("@" + creator + ", tell your friends to get to this bot and enter following pin: " + pin).build());
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                }
            } else if (messageFromInput.startsWith("#")) {
                boolean isSuccessful = false;
                String friendName = "";
                for (Lobby lobby : lobbies) {
                    if (lobby.m_pin.equals(messageFromInput)) {
                        lobby.m_playerNameToChatId.put(currentUser,chatId);
                        isSuccessful = true;
                        friendName = lobby.m_creator;
                        break;
                    }
                }
                SendMessage sendMessage;
                if (!isSuccessful) {
                  sendMessage=SendMessage.builder().chatId(chatId).text("Try asking your friend the pin once again.\nYou typed: " + messageFromInput).build();
                } else {
                    sendMessage=SendMessage.builder().chatId(chatId).text("You have been successfully added to the @" + friendName + " lobby").build();
                }
                try {
                    execute(sendMessage);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            } else if (messageFromInput.equals(startGame)) {
                int size = playerNameToChatId.size();
                if (size < 5) {
                    FoolPlayer[] players = new FoolPlayer[size];
                    for (int i = 0; i < size; i++) {
                        Object[] playersName = playerNameToChatId.keySet().toArray();
                        players[i] = new FoolPlayer(playersName[i].toString());
                    }
                    //PharaohLogic game = new PharaohLogic(players, new Deck(DeckType.MEDIUM));
                    GameLogicToBot gameLogicToBot=new GameLogicToBot(this);
                    FoolLogic game = new FoolLogic(players, new Deck(DeckType.MEDIUM), gameLogicToBot);
                    this.gameLogic = game;
                    this.gameLogic.startGame();
                }
            }
        }
    }

    public String getInputToGameLogic(){
        return null;
    }

    public void sendOutputToUser(String playerName, String[] availableCommands){
        // find player's chatId by playerName
        currentAvailableCommands=availableCommands;
        String chatId= playerNameToChatId.get(playerName);
        SendMessage message = SendMessage
                .builder()
                .chatId(chatId)
                .build();

        ReplyKeyboardMarkup replyKeyboardMarkup=new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboardRows=new ArrayList<>();
        for (String command : availableCommands){
            KeyboardRow keyboardRow = new KeyboardRow();
            keyboardRow.add(command);
            keyboardRows.add(keyboardRow);
        }
        replyKeyboardMarkup.setKeyboard(keyboardRows);
        message.setReplyMarkup(replyKeyboardMarkup);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private ReplyKeyboardMarkup getStartReplyKeyboardMarkup() {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();

        List<KeyboardRow> keyboardRows = new ArrayList<>();

        KeyboardRow row = new KeyboardRow();
        row.add("/start");
        row.add("/help");
        row.add("/create_lobby_pharaoh");
        row.add("/create_lobby_fool");
        row.add("/start_game");

        keyboardRows.add(row);

        keyboardMarkup.setKeyboard(keyboardRows);
        return keyboardMarkup;
    }

    private void sendMessageToUser(String message, String chatId) {
        SendMessage sendMessage = getSendMessage(message, chatId);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private SendMessage getSendMessage(String message, String chatId) {
        SendMessage sendMessage = SendMessage
                .builder()
                .chatId(String.valueOf(chatId))
                .text(message)
                .build();
        return sendMessage;
    }
}
