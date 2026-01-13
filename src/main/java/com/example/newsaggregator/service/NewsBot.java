package com.example.newsaggregator.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
public class NewsBot extends TelegramLongPollingBot {

    @Value("${bot.name}")
    private String botName;

    // Временное хранилище подписчиков (в памяти). При перезапуске исчезнет.
    private final Set<Long> subscribers = Collections.newSetFromMap(new ConcurrentHashMap<>());

    public NewsBot(@Value("${bot.token}") String botToken) {
        super(botToken);
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String text = update.getMessage().getText();
            Long chatId = update.getMessage().getChatId();

            if ("/start".equals(text)) {
                subscribers.add(chatId);
                sendMessage(chatId, "Привет! Я буду присылать тебе свежие новости.");
            }
        }
    }

    public void broadcastNews(String messageText) {
        for (Long chatId : subscribers) {
            sendMessage(chatId, messageText);
        }
    }

    private void sendMessage(Long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(text);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error("Error sending message to {}", chatId, e);
        }
    }

    @Override
    public String getBotUsername() {
        return botName;
    }
}
