package com.example.experiment1.domain;
import java.time.Instant;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor

@Entity
@Table(name = "polls")
public class Poll {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String pollId;
    private String question;
    @OneToMany(mappedBy = "poll", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<VoteOption> options;
    private Instant publishedAt;
    private Instant validUntil;
    private String createdBy;
    @ManyToOne
    @JoinColumn(name = "created_by_user_id")
    private User createdByUser;

         /**
     *
     * Adds a new option to this Poll and returns the respective
     * VoteOption object with the given caption.
     * The value of the presentationOrder field gets determined
     * by the size of the currently existing VoteOptions for this Poll.
     * I.e. the first added VoteOption has presentationOrder=0, the secondly
     * registered VoteOption has presentationOrder=1 ans so on.
     */
    public VoteOption addVoteOption(String caption) {

         if (options == null) {
            options = new java.util.ArrayList<>();
         }
         int order = options.size();
         VoteOption option = new VoteOption();
         option.setCaption(caption);
         option.setPresentationOrder(order);
         option.setPoll(this);
         options.add(option);
         return option;
    }
}

