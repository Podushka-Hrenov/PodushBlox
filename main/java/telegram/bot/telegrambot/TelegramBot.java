package telegram.bot.telegrambot;

import org.apache.commons.io.FilenameUtils;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;
import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class TelegramBot {
    private Map<String, ActionEvent> actionHandlers, fileHandlers;
    public OkHttpTelegramClient client;

    public static final Map<Long, User> users = new HashMap<>();

    public TelegramBot(String botToken) throws TelegramApiException {
        client = new OkHttpTelegramClient(botToken);
        
        actionHandlers = new HashMap<>();
        fileHandlers = new HashMap<>();

        registerBot(botToken);
    }

    private void registerBot(String botToken) throws TelegramApiException {
        TelegramBotsLongPollingApplication app = new TelegramBotsLongPollingApplication();

        app.registerBot(botToken, new LongPollingUpdateConsumer() {
            @Override
            public void consume(List<Update> updates) {
                handleUpdate(updates.get(updates.size()-1));
            }
        });
    }

    private void handleUpdate(Update update) {
        if (update.hasMessage()) {
            Message message = update.getMessage();
            Long userId = message.getFrom().getId();

            users.computeIfAbsent(userId, k -> new User(userId));

            if (message.hasDocument()) {
                System.out.println("new document!");

                String extension = FilenameUtils.getExtension(message.getDocument().getFileName());

                if (fileHandlers.containsKey(extension)) {
                    fileHandlers.get(extension).onceWithCooldown(
                        update, userId
                    );
                }
            } else if (message.hasText()) {
                System.out.println("new action!");

                String actionName = message.getText();

                if (actionHandlers.containsKey(actionName)) {
                    actionHandlers.get(actionName).onceWithCooldown(
                        update, userId
                    );
                }
            }
        } else if (update.hasInlineQuery()) {

        }
    }

    public void SendMessage(Long chatId, String text) throws TelegramApiException {
        client.execute(SendMessage.builder()
            .chatId(chatId)
            .text(text)
            .build()
        );
    }

    public void SendPhoto(Long chatId, File photo) throws TelegramApiException {
        client.execute(SendPhoto.builder()
            .chatId(chatId)
            .photo(new InputFile(photo))
            .build()
        );
    }

    public void subscribeAction(String name, Consumer<Update> handler, String extender, long cooldown) {
        actionHandlers.put(name, new ActionEvent(name, handler, extender, cooldown));
    }

    public void subscribeFile(String extension, Consumer<Update> handler, String extender, long cooldown) {
        fileHandlers.put(extension, new ActionEvent(extension, handler, extender, cooldown));
    }
}