package telegram.bot.telegrambot;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Properties;

public class MessagesStorage {
    private final HashMap<String, Properties> languageProperties = new HashMap<>();

    public MessagesStorage() throws IOException {
        loadLanguage("ru");
    }

    private void loadLanguage(String lang) throws IOException {
        InputStream input = getClass().getResourceAsStream("/messages_" + lang + ".properties");
        
        Properties props = new Properties();
        props.load(new InputStreamReader(input, StandardCharsets.UTF_8));

        languageProperties.put(lang, props);
    }

    public String get(String key, String lang) {
        return languageProperties.get(lang).getProperty(key);
    }

    public String get(String key, String lang, Object... args) {
        return String.format(get(key, lang), args);
    }
}
