package com.example.newsaggregator.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Сущность "Подписчик".
 * Хранит информацию о пользователе Telegram, подписавшемся на рассылку.
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Subscriber {

    /**
     * Уникальный идентификатор записи в БД.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * ID чата в Telegram (уникальное поле).
     */
    @Column(unique = true)
    private Long chatId;

    /**
     * Конструктор для создания подписчика только по ID чата.
     *
     * @param chatId ID чата в Telegram.
     */
    public Subscriber(Long chatId) {
        this.chatId = chatId;
    }
}
