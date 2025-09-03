package com.example.experiment1.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class VoteOption {
    private String caption;
    private int presentationOrder;
}
