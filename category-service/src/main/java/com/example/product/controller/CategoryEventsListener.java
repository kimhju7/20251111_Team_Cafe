package com.example.product.controller;

import com.example.product.repository.CategoryRepository;
import jakarta.transaction.Transactional;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CategoryEventsListener {

    private final CategoryRepository categoryRepository;

    @RabbitListener(queues = "category-service.post-events")
    @Transactional
    public void onPostEvent(PostEvent event) {
        if (event.getCategoryId() == null) return;
        if ("PostCreated".equals(event.getEventType())) {
            categoryRepository.findById(event.getCategoryId()).ifPresent(c -> {
                c.setPostCount((c.getPostCount() == null ? 0 : c.getPostCount()) + 1);
                categoryRepository.save(c);
            });
        } else if ("PostDeleted".equals(event.getEventType())) {
            categoryRepository.findById(event.getCategoryId()).ifPresent(c -> {
                int current = (c.getPostCount() == null ? 0 : c.getPostCount());
                c.setPostCount(Math.max(0, current - 1));
                categoryRepository.save(c);
            });
        }
    }

    @Data
    public static class PostEvent {
        private String eventId;
        private String eventType;
        private int eventVersion;
        private Long postId;
        private Long categoryId;
    }
}


