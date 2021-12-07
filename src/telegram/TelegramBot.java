package telegram;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

public class TelegramBot extends TelegramLongPollingBot {
    private final String token="5008512617:AAELuxvMo_D0hg1C8pHiRN52NYWhewlHgAw";
    private final String botUsername="Card Games";

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
        if (update.hasMessage() && update.getMessage().hasText()){
            String message=update.getMessage().getText();
            long chatId=update.getMessage().getChatId();
            System.out.println();
            if (message.equals("D 5")){
                sendMessageToUser("Diamond 5", chatId);
                return;
            } else if(message.equals("/markup")){
                SendMessage sendMessage=SendMessage
                        .builder()
                        .chatId(String.valueOf(chatId))
                        .text("Keyboard")
                        .build();

                ReplyKeyboardMarkup replyKeyboardMarkup=new ReplyKeyboardMarkup();

                List<KeyboardRow> keyBoardRowList =new ArrayList<>();

                KeyboardRow row1=new KeyboardRow();
                row1.add("Row 1 btn 1");
                row1.add("Row 1 btn 2");
                row1.add("Row 1 btn 3");

                KeyboardRow row2=new KeyboardRow();
                row2.add("Row 2 btn 1");
                row2.add("Row 2 btn 2");
                row2.add("Row 2 btn 3");

                keyBoardRowList.add(row1);
                keyBoardRowList.add(row2);

                replyKeyboardMarkup.setKeyboard(keyBoardRowList);

                sendMessage.setReplyMarkup(replyKeyboardMarkup);

                try {
                    execute(sendMessage);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }else if (update.getMessage().getChat().getUserName().equals("oh_mssblvr") || update.getMessage().getChat().getUserName().equals("panda2panda") || update.getMessage().getChat().getUserName().equals("DrKisha")){
                try {
                    execute(SendMessage.builder().chatId(String.valueOf(update.getMessage().getChat().getId())).text("Ты жопа").build());
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
                return;
            }
            else if (message.equals("Row 1 btn 2")){
                try {
                    execute(SendMessage.builder().chatId(String.valueOf(chatId)).text("text").build());
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
                return;
            } //else if(update.getMessage().getChatId().){

            //}
            else {
                try {
                    execute(SendMessage.builder().chatId(String.valueOf(chatId)).text("fck u").build());
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
                return;
            }
            sendMessageToUser(message, chatId);
        }

    }

    private void sendMessageToUser(String message, long chatId) {
        SendMessage sendMessage = getSendMessage(message, chatId);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private SendMessage getSendMessage(String message, long chatId) {
        SendMessage sendMessage= SendMessage
                .builder()
                .chatId(String.valueOf(chatId))
                .text(message)
                .build();
        return sendMessage;
    }
}
