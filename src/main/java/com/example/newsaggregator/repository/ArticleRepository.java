package com.example.newsaggregator.repository;

import com.example.newsaggregator.model.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Репозиторий для работы с сущностью {@link Article}.
 */
@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {

    /**
     * Поиск статей, содержащих ключевое слово в заголовке или тексте (без учета
     * регистра).
     *
     * @param title    Ключевое слово для поиска в заголовке.
     * @param content  Ключевое слово для поиска в контенте.
     * @param pageable Информация о пагинации.
     * @return Страница найденных статей.
     */
    Page<Article> findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(String title, String content,
            Pageable pageable);
}
