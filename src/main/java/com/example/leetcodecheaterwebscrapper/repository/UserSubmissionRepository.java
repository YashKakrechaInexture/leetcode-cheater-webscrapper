package com.example.leetcodecheaterwebscrapper.repository;

import com.example.leetcodecheaterwebscrapper.models.UserSubmission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserSubmissionRepository extends JpaRepository<UserSubmission,Long> {
}
