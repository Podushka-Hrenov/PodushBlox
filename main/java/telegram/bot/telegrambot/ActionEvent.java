package telegram.bot.telegrambot;

import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class ActionEvent {
    private Map<Long, Long> cdMap = new HashMap<>();
    private String extender, name; 
    public Consumer<Update> handler;
    public long cooldown;

    public ActionEvent(String name, Consumer<Update> handler, String extender, long cooldown) {
        this.handler = handler;
        this.extender = extender;
        this.name = name;
        this.cooldown = cooldown;
    }

    public void onceWithCooldown(Update arg0, Long userId) {
        if (!isExtends(userId)) return;
        long currentTime = System.currentTimeMillis();

        if (cdMap.containsKey(userId)) {
            Long elapsedTime = currentTime - cdMap.get(userId);
            if (elapsedTime < cooldown) return;
        }

        handler.accept(arg0);
        cdMap.put(userId, currentTime);

        TelegramBot.users.get(userId).status = name;
    }

    public void once(Update arg0, Long userId) {
        if (!isExtends(userId)) return;
        handler.accept(arg0);
    }

    private boolean isExtends(Long userId) {
        return (extender == null || TelegramBot.users.get(userId).status.equals(extender));
    }
}