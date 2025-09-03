package com.example.experiment1.domain;

import java.time.Instant;

import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
public class Vote {
    private String userId;
    private String pollId;
    private int optionIndex;
    private Instant publishedAt;
}
