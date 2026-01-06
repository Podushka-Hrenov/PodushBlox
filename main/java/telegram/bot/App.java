package telegram.bot;

import static java.lang.Math.PI;
import static java.lang.Math.abs;
import static java.lang.Math.cos;
import static java.lang.Math.min;
import static java.lang.Math.sin;

import java.io.File;
import java.io.IOException;

import org.checkerframework.checker.units.qual.min;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Document;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import telegram.bot.globals.*;
import telegram.bot.objects.*;
import telegram.bot.telegrambot.MessagesStorage;
import telegram.bot.telegrambot.TelegramBot;

public class App {
    public static Workspace workspace = new Workspace();

    private static float min(float... numbers) {
        float min = numbers[0];
        for (float num : numbers) {
            if (num < min) min = num;
        }
        return min;
    }

    private static float max(float... numbers) {
        float max = numbers[0];
        for (float num : numbers) {
            if (num > max) max = num;
        }
        return max;
    }

    public static void main(String[] args) throws TelegramApiException, IOException {
        TelegramBot bot = new TelegramBot("8222830320:AAEt4UUz7b7WBEMqXRr5yqiWr4FJzm2nA78");
        MessagesStorage messagesStorage = new MessagesStorage();

        Camera camera = new Camera((float)PI/2, 10f, 100f, new Vec2(1e3f, 1e3f));
        camera.SetParent(workspace);

        bot.subscribeAction("/import", (update) -> {  
            SendMessage message = SendMessage.builder()
                .chatId(update.getMessage().getChatId())
                .text(messagesStorage.get("import_model.info", "ru"))
                .build();
            
            try {bot.client.execute(message);
            } catch (TelegramApiException e) {}

        }, null, 5_000L);

        bot.subscribeFile("obj", (update) -> {
            Message message = update.getMessage();
            Document document = message.getDocument();

            GetFile getFile = new GetFile(document.getFileId());
            
            try {
                String filePath = bot.client.execute(getFile).getFilePath();
                File file = bot.client.downloadFile(filePath);

                MeshPart meshPart = new MeshPart(file);
                meshPart.SetParent(workspace);
                meshPart.CFrame = new CFrame(0, 0, 0, 0, (float)sin(PI/4), (float)sin(PI/4));

                Vec3 AABB = meshPart.MeshData().getAABB();
                AABB = Vec3.one.muln(max(AABB.x, AABB.y, AABB.z));

                Vec3 direction = new Vec3(0, (float)sin(PI/4), 0);
                float dist = min(
                    AABB.x / abs(direction.x), 
                    AABB.y / abs(direction.y),
                    AABB.z / abs(direction.z)
                );
                
                camera.CFrame = new CFrame(0, 0, -(dist/2+3));

                SendPhoto sendPhoto = SendPhoto.builder()
                    .chatId(message.getChatId())
                    .photo(new InputFile(camera.render()))
                    .build();
                
                sendPhoto.setReplyToMessageId(message.getMessageId());
                bot.client.execute(sendPhoto);

                meshPart.SetParent(null);
            } catch (TelegramApiException e) {
            } catch (IOException e) {
                try {
                    bot.SendMessage(message.getChatId(),
                        messagesStorage.get("error.read_file", "ru")
                    );
                } catch (TelegramApiException e1) {}
            }

        }, null, 5_000L);

        try {Thread.currentThread().join();} catch (InterruptedException e) {}
    }
}
