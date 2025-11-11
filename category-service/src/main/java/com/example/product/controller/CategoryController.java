package com.example.product.controller;

import com.example.product.model.Category;
import com.example.product.repository.CategoryRepository;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/categorys")
public class CategoryController {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private TopicExchange blogEventsExchange;

    @GetMapping
    public List<Category> getAllCategorys() {
        return categoryRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Category> getCategoryById(@PathVariable Long id) {
        return categoryRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Category createCategory(@RequestBody Category category) {
        return categoryRepository.save(category);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Category> updateCategory(@PathVariable Long id, @RequestBody Category category) {
        return categoryRepository.findById(id)
                .map(existingProduct -> {
                    category.setId(id);
                    return ResponseEntity.ok(categoryRepository.save(category));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        return categoryRepository.findById(id)
                .map(product -> {
                    categoryRepository.delete(product);

                    CategoryDeletedEvent evt = new CategoryDeletedEvent();
                    evt.setEventId(UUID.randomUUID().toString());
                    evt.setCategoryId(id);
                    evt.setOccurredAt(Instant.now().toString());
                    rabbitTemplate.convertAndSend(blogEventsExchange.getName(), "category.deleted", evt);

                    return ResponseEntity.ok().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }

    public static class CategoryDeletedEvent {
        private String eventId;
        private String eventType = "CategoryDeleted";
        private int eventVersion = 1;
        private Long categoryId;
        private String occurredAt;

        public String getEventId() { return eventId; }
        public void setEventId(String eventId) { this.eventId = eventId; }
        public String getEventType() { return eventType; }
        public void setEventType(String eventType) { this.eventType = eventType; }
        public int getEventVersion() { return eventVersion; }
        public void setEventVersion(int eventVersion) { this.eventVersion = eventVersion; }
        public Long getCategoryId() { return categoryId; }
        public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }
        public String getOccurredAt() { return occurredAt; }
        public void setOccurredAt(String occurredAt) { this.occurredAt = occurredAt; }
    }
}