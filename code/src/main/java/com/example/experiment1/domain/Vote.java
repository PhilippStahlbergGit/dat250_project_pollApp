package com.example.experiment1.domain;

import java.time.Instant;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "votes")
public class Vote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userId;
    private String pollId;
    private int optionIndex;
    private Instant publishedAt;
    @ManyToOne
    @JoinColumn(name = "vote_option_id")
    private VoteOption votesOn;
    
}
