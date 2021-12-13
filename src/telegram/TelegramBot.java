package telegram;

import com.common.deck.Deck;
import com.common.deck.DeckType;
import com.common.player.BasePlayer;
import com.games.fool.FoolLogic;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TelegramBot extends TelegramLongPollingBot {
    public List<BasePlayer> playerList=new ArrayList<>();
    public FoolLogic gameLogic;
    private List<Lobby> lobbies=new ArrayList<>();
    private final String token = "5008512617:AAELuxvMo_D0hg1C8pHiRN52NYWhewlHgAw";
    private final String botUsername = "Card Games";

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
                String pin = "#" + Integer.toHexString(new Random().nextInt(10000, 99999));
                String creator = currentUser;
                if (strings[strings.length - 1].equals("fool")) {
                    playerList = new ArrayList<>();

                    BasePlayer player = new BasePlayer();
                    player.name = currentUser;

                    playerList.add(player);
                    lobbies.add(new Lobby(creator, pin, playerList));
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
                        BasePlayer player = new BasePlayer();
                        player.name = currentUser;
                        lobby.m_playerList.add(player);
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
                int size = playerList.size();
                if (size < 5) {
                    BasePlayer[] players = new BasePlayer[size];
                    for (int i = 0; i < size; i++) {
                        players[i] = playerList.get(i);
                    }
                    //PharaohLogic game = new PharaohLogic(players, new Deck(DeckType.MEDIUM));
                    FoolLogic game = new FoolLogic(players, new Deck(DeckType.MEDIUM));
                    this.gameLogic = game;
                    this.gameLogic.startGame();
                }
            }
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
