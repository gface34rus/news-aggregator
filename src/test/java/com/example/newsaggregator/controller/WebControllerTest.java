package com.example.newsaggregator.controller;

import com.example.newsaggregator.model.Article;
import com.example.newsaggregator.repository.ArticleRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(WebController.class)
class WebControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ArticleRepository articleRepository;

    @Test
    void index_ShouldReturnArticlesPage() throws Exception {
        // Arrange
        Article article = new Article();
        article.setTitle("Web Test Article");
        Page<Article> page = new PageImpl<>(List.of(article));

        when(articleRepository.findAll(any(Pageable.class))).thenReturn(page);

        // Act & Assert
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("articles"))
                .andExpect(content().string(containsString("Web Test Article")));
    }

    @Test
    void index_WithKeyword_ShouldSearch() throws Exception {
        // Arrange
        String keyword = "Java";
        Article article = new Article();
        article.setTitle("Java News");
        Page<Article> page = new PageImpl<>(List.of(article));

        when(articleRepository.findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(
                eq(keyword), eq(keyword), any(Pageable.class))).thenReturn(page);

        // Act & Assert
        mockMvc.perform(get("/").param("keyword", keyword))
                .andExpect(status().isOk())
                .andExpect(model().attribute("keyword", keyword))
                .andExpect(content().string(containsString("Java News")));
    }
}
