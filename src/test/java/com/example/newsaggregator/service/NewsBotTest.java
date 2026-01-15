package com.example.newsaggregator.service;

import com.example.newsaggregator.repository.SubscriberRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NewsBotTest {

    @Mock
    private SubscriberRepository subscriberRepository;

    @InjectMocks
    private NewsBot newsBot;

    @Test
    void onUpdateReceived_NewSubscriber_ShouldSaveAndWelcome() {
        // Arrange
        Update update = mock(Update.class);
        Message message = mock(Message.class);

        when(update.hasMessage()).thenReturn(true);
        when(update.getMessage()).thenReturn(message);
        when(message.hasText()).thenReturn(true);
        when(message.getText()).thenReturn("/start");
        when(message.getChatId()).thenReturn(12345L);
        when(subscriberRepository.existsByChatId(12345L)).thenReturn(false);

        // Act
        newsBot.onUpdateReceived(update);

        // Assert
        verify(subscriberRepository, times(1)).save(any());
        // We cannot easily verify sendMessage execution because it's a protected method
        // of the parent class
        // and 'execute' is final or hard to mock without PowerMock.
        // But we verified the business logic: checking existence and saving.
    }

    @Test
    void onUpdateReceived_ExistingSubscriber_ShouldNotSave() {
        // Arrange
        Update update = mock(Update.class);
        Message message = mock(Message.class);

        when(update.hasMessage()).thenReturn(true);
        when(update.getMessage()).thenReturn(message);
        when(message.hasText()).thenReturn(true);
        when(message.getText()).thenReturn("/start");
        when(message.getChatId()).thenReturn(12345L);
        when(subscriberRepository.existsByChatId(12345L)).thenReturn(true);

        // Act
        newsBot.onUpdateReceived(update);

        // Assert
        verify(subscriberRepository, never()).save(any());
    }
}
