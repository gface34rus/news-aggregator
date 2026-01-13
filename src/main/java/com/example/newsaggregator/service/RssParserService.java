package com.example.newsaggregator.service;

import com.example.newsaggregator.model.Article;
import com.example.newsaggregator.repository.ArticleRepository;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RssParserService {

    private final ArticleRepository articleRepository;
    private final NewsBot newsBot;

    @Scheduled(fixedRate = 600000) // 10 minutes
    public void parseRss() {
        String rssUrl = "https://habr.com/ru/rss/all/all/";
        log.info("Starting RSS parsing: {}", rssUrl);

        try {
            URL feedUrl = new URL(rssUrl);
            SyndFeedInput input = new SyndFeedInput();
            SyndFeed feed = input.build(new XmlReader(feedUrl));

            List<Article> existingArticles = articleRepository.findAll();

            for (SyndEntry entry : feed.getEntries()) {
                String link = entry.getLink();

                boolean exists = existingArticles.stream()
                        .anyMatch(a -> a.getUrl().equals(link));

                if (!exists) {
                    Article article = new Article();
                    article.setTitle(entry.getTitle());
                    article.setUrl(link);
                    article.setContent(entry.getDescription() != null ? entry.getDescription().getValue() : "");

                    Date pubDate = entry.getPublishedDate();
                    if (pubDate != null) {
                        article.setPublishedAt(pubDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
                    } else {
                        article.setPublishedAt(LocalDateTime.now());
                    }

                    articleRepository.save(article);
                    log.info("Saved article: {}", article.getTitle());

                    newsBot.broadcastNews("Новая статья: " + article.getTitle() + "\n" + article.getUrl());
                }
            }
            log.info("RSS parsing completed.");

        } catch (Exception e) {
            log.error("Error parsing RSS", e);
        }
    }
}
