package com.example.newsaggregator.repository;

import com.example.newsaggregator.model.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {
    Page<Article> findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(String title, String content,
            Pageable pageable);
}
