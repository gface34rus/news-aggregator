package com.example.newsaggregator.controller;

import com.example.newsaggregator.model.Article;
import com.example.newsaggregator.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class WebController {

    private final ArticleRepository articleRepository;

    @GetMapping("/")
    public String index(Model model,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        Page<Article> articlePage;
        if (keyword != null && !keyword.isEmpty()) {
            articlePage = articleRepository.findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(keyword,
                    keyword,
                    PageRequest.of(page, size, Sort.by("publishedAt").descending()));
            model.addAttribute("keyword", keyword);
        } else {
            articlePage = articleRepository.findAll(
                    PageRequest.of(page, size, Sort.by("publishedAt").descending()));
        }

        model.addAttribute("articles", articlePage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", articlePage.getTotalPages());

        return "articles";
    }
}
