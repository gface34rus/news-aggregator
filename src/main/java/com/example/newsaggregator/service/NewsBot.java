package com.example.newsaggregator.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/**
 * Telegram-бот для рассылки новостей.
 * Обрабатывает команды пользователей и рассылает уведомления о новых статьях.
 */
@Component
@Slf4j
public class NewsBot extends TelegramLongPollingBot {

    @Value("${bot.name}")
    private String botName;

    private final com.example.newsaggregator.repository.SubscriberRepository subscriberRepository;

    public NewsBot(@Value("${bot.token}") String botToken,
            com.example.newsaggregator.repository.SubscriberRepository subscriberRepository) {
        super(botToken);
        this.subscriberRepository = subscriberRepository;
    }

    /**
     * Метод, вызываемый при получении обновления от Telegram (сообщение, команда).
     * Обрабатывает команду /start для регистрации подписчика.
     *
     * @param update Объект обновления Telegram.
     */
    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String text = update.getMessage().getText();
            Long chatId = update.getMessage().getChatId();

            if ("/start".equals(text)) {
                if (!subscriberRepository.existsByChatId(chatId)) {
                    subscriberRepository.save(new com.example.newsaggregator.model.Subscriber(chatId));
                    sendMessage(chatId, "Привет! Я буду присылать тебе свежие новости.");
                    log.info("New subscriber registered: {}", chatId);
                } else {
                    sendMessage(chatId, "Вы уже подписаны на новости.");
                }
            }
        }
    }

    /**
     * Рассылка сообщения всем подписчикам.
     *
     * @param messageText Текст сообщения.
     */
    public void broadcastNews(String messageText) {
        var subscribers = subscriberRepository.findAll();
        for (com.example.newsaggregator.model.Subscriber sub : subscribers) {
            sendMessage(sub.getChatId(), messageText);
        }
    }

    /**
     * Вспомогательный метод для отправки сообщения.
     *
     * @param chatId ID чата получателя.
     * @param text   Текст сообщения.
     */
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
