package com.example.newsaggregator.repository;

import com.example.newsaggregator.model.Subscriber;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Репозиторий для работы с сущностью {@link Subscriber}.
 */
public interface SubscriberRepository extends JpaRepository<Subscriber, Long> {

    /**
     * Проверяет, существует ли подписчик с заданным chatId.
     *
     * @param chatId ID чата в Telegram.
     * @return true, если подписчик существует, иначе false.
     */
    boolean existsByChatId(Long chatId);
}
