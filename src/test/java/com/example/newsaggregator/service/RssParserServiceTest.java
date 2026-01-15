package com.example.newsaggregator.service;

import com.example.newsaggregator.model.Article;
import com.example.newsaggregator.repository.ArticleRepository;
import com.rometools.rome.feed.synd.SyndContent;
import com.rometools.rome.feed.synd.SyndContentImpl;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndEntryImpl;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.feed.synd.SyndFeedImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RssParserServiceTest {

    @Mock
    private ArticleRepository articleRepository;

    @Mock
    private NewsBot newsBot;

    @Spy
    @InjectMocks
    private RssParserService rssParserService;

    @Test
    void parseRss_NewArticle_ShouldSaveAndBroadcast() throws Exception {
        // Arrange
        SyndFeed mockFeed = new SyndFeedImpl();
        SyndEntry entry = new SyndEntryImpl();
        entry.setTitle("Test Title");
        entry.setLink("http://test.com/1");
        SyndContent description = new SyndContentImpl();
        description.setValue("Test Description");
        entry.setDescription(description);
        mockFeed.setEntries(List.of(entry));

        doReturn(mockFeed).when(rssParserService).fetchFeed(anyString());
        when(articleRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        rssParserService.parseRss();

        // Assert
        verify(articleRepository, times(1)).save(any(Article.class));
        verify(newsBot, times(1)).broadcastNews(contains("Test Title"));
    }

    @Test
    void parseRss_ExistingArticle_ShouldDuplicate() throws Exception {
        // Arrange
        SyndFeed mockFeed = new SyndFeedImpl();
        SyndEntry entry = new SyndEntryImpl();
        entry.setTitle("Test Title");
        entry.setLink("http://test.com/1");
        mockFeed.setEntries(List.of(entry));

        Article existing = new Article();
        existing.setUrl("http://test.com/1");

        doReturn(mockFeed).when(rssParserService).fetchFeed(anyString());
        when(articleRepository.findAll()).thenReturn(List.of(existing));

        // Act
        rssParserService.parseRss();

        // Assert
        verify(articleRepository, never()).save(any(Article.class));
        verify(newsBot, never()).broadcastNews(anyString());
    }
}
