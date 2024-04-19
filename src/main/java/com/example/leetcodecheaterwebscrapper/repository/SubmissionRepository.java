package com.example.leetcodecheaterwebscrapper.repository;

import com.example.leetcodecheaterwebscrapper.models.Submission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubmissionRepository extends JpaRepository<Submission,Long> {
}
