package com.example.newsaggregator.repository;

import com.example.newsaggregator.model.Subscriber;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubscriberRepository extends JpaRepository<Subscriber, Long> {
    boolean existsByChatId(Long chatId);
}
