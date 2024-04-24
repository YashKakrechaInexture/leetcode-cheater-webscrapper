package com.example.leetcodecheaterwebscrapper.models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table
public class Submission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String language;

    private String submissionTime;

    @Column(length = 100000)
    private String code;

    private String codeHash;
}
