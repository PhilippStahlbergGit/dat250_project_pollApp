package com.example.experiment1.domain.jpa;

import java.time.Instant;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringExclude;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor

@Entity
@Table(name = "polls")
public class Poll {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	// duplication?
	private String pollId;

	private String question;

	@OneToMany(mappedBy = "poll", cascade = CascadeType.ALL)
	@JsonManagedReference("poll-option")
	private List<VoteOption> options;

	private Instant publishedAt;

	private Instant validUntil;

	// duplication?
	private String createdBy;

	// circular reference between User & Poll
	// thereby we add exclude to not calculate them circular
	@ToString.Exclude
	@EqualsAndHashCode.Exclude
	@ManyToOne
	@JsonBackReference("user-poll")
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
