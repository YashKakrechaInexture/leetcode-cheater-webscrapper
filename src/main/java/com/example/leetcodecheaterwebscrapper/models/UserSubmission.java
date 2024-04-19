package com.example.leetcodecheaterwebscrapper.models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table
public class UserSubmission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private int userRank;

    private String username;

    private int score;

    private String finishTime;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private Submission questionOneSubmission;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private Submission questionTwoSubmission;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private Submission questionThreeSubmission;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private Submission questionFourSubmission;
}
