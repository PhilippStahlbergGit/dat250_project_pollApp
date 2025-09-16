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
         // TODO: implement
         int order = options.size();
         VoteOption option = new VoteOption();
         option.setCaption(caption);
         option.setPresentationOrder(order);
         options.add(option);
         return option;
    }
}

