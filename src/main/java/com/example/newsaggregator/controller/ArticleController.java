package com.example.newsaggregator.controller;

import com.example.newsaggregator.model.Article;
import com.example.newsaggregator.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/articles")
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleRepository articleRepository;

    @GetMapping
    public Page<Article> getAllArticles(
            @PageableDefault(sort = "publishedAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return articleRepository.findAll(pageable);
    }
}
