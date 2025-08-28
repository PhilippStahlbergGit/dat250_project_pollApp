package com.example.experiment1.domain;

import java.time.Instant;

public class Vote {
    private Instant publishedAt;
    
    public Vote() {}

    public Instant getPublishedAt() {
        return this.publishedAt;
    }
    public void setPublishedAt( Instant publishedAt ) {
        this.publishedAt = publishedAt;
    }
}
