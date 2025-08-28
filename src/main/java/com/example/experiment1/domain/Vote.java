package com.example.experiment1.domain;

import java.time.Instant;

public class Vote {
    private String userId;
    private String pollId;
    private int optionIndex;
    private Instant publishedAt;
    
    public Vote() {}

    public String getUserId() {
        return userId;
    }
    public void setUserId( String userId ){
        this.userId = userId;
    }

    public String getPollId() {
        return pollId;
    }
    public void setPollId( String pollId ) {
        this.pollId = pollId;
    }

    public int getOptionIndex() {
        return optionIndex;
    }
    public void setOptionIndex( int optionIndex ) {
        this.optionIndex = optionIndex;
    }

    public Instant getPublishedAt() {
        return this.publishedAt;
    }
    public void setPublishedAt( Instant publishedAt ) {
        this.publishedAt = publishedAt;
    }
}
