package com.example.experiment1.domain;
import java.time.Instant;

public class Poll {
    private String pollId;
    private String question;
    private Instant publishedAt;
    private Instant validUntil;
    private String createdBy;

    public Poll() {}

    public String getPollId() {
        return this.pollId;
    }
    public void setPollId( String pollId ) {
        this.pollId = pollId;
    }

    public String getQuestion() {
        return this.question;
    }
    public void setQuestion( String question ) {
        this.question = question;
    }
    
    public Instant getPublishedAt() {
        return publishedAt;
    }
    public void setPublishedAt( Instant publishedAt ) {
        this.publishedAt = publishedAt;
    }

    public Instant getValidUntil() {
        return this.validUntil;
    }
    public void setValidUntil( Instant validUntil ) {
        this.validUntil = validUntil;
    }

    public String getCreatedBy() {
        return createdBy;
    }
    public void setCreatedBy( String createdBy ) {
        this.createdBy = createdBy;
    }


}
