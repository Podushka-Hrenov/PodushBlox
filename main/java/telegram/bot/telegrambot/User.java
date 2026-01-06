package telegram.bot.telegrambot;

public class User {
    public String status = "Idle";
    public Long userId;

    public User(Long userId) {
        this.userId = userId;
    }
}
