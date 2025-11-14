package com.example.experiment1.domain.jpa;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@Entity
@Table(name = "voteoptions")
public class VoteOption {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	private String caption;
	private int presentationOrder;
	private int votes;

	// look at Poll for documentation on this
	@ToString.Exclude
	@EqualsAndHashCode.Exclude
	@ManyToOne
	@JsonBackReference("poll-option")
	private Poll poll;
}
