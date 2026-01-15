package com.example.newsaggregator.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

/**
 * Сущность "Статья".
 * Представляет новость, полученную из RSS-ленты.
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Article {

    /**
     * Уникальный идентификатор статьи.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Заголовок новости.
     */
    private String title;

    /**
     * Краткое описание или содержимое новости.
     * Хранится как TEXT в базе данных.
     */
    @Column(columnDefinition = "TEXT")
    private String content;

    /**
     * Ссылка на оригинал статьи.
     */
    private String url;

    /**
     * Дата публикации новости.
     */
    private LocalDateTime publishedAt;
}
