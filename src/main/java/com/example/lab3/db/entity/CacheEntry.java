package com.example.lab3.db.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "cache_entries", uniqueConstraints = @UniqueConstraint(columnNames = "cache_key"))
public class CacheEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "cache_key", nullable = false, length = 512)
    private String key;

    @Column(name = "cache_value", columnDefinition = "CLOB")
    private String value;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    public CacheEntry() {}

    public CacheEntry(String key, String value, LocalDateTime createdAt) {
        this.key = key;
        this.value = value;
        this.createdAt = createdAt;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getKey() { return key; }
    public void setKey(String key) { this.key = key; }
    public String getValue() { return value; }
    public void setValue(String value) { this.value = value; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}