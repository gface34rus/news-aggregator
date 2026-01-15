package com.example.newsaggregator.repository;

import com.example.newsaggregator.model.Article;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class RepositoryTest {

    @Autowired
    private ArticleRepository articleRepository;

    @Test
    void findByTitleOrContent_ShouldFindIgnoreCase() {
        // Arrange
        Article article = new Article();
        article.setTitle("Spring Boot Release");
        article.setContent("New features in version 3.2");
        article.setUrl("http://example.com");
        article.setPublishedAt(LocalDateTime.now());
        articleRepository.save(article);

        // Act
        Page<Article> result = articleRepository.findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(
                "spring", "spring", PageRequest.of(0, 10));

        // Assert
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getTitle()).isEqualTo("Spring Boot Release");
    }

    @Test
    void findByTitleOrContent_NotFound_ShouldReturnEmpty() {
        // Act
        Page<Article> result = articleRepository.findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(
                "Python", "Python", PageRequest.of(0, 10));

        // Assert
        assertThat(result.getContent()).isEmpty();
    }
}
