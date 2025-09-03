package com.example.experiment1.domain;
import java.time.Instant;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Poll {
    private String pollId;
    private String question;
    private List<VoteOption> options;
    private Instant publishedAt;
    private Instant validUntil;
    private String createdBy;
}
