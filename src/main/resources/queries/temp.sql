SELECT * FROM leetcode_cheater_webscrap.user_submission;

SELECT COUNT(*) FROM leetcode_cheater_webscrap.user_submission;

SELECT * FROM leetcode_cheater_webscrap.user_submission where username='username';

SELECT * FROM leetcode_cheater_webscrap.user_submission where question_one_submission_id in (0) OR
    question_two_submission_id in (0) OR
    question_three_submission_id in (0) OR
    question_four_submission_id in (0)
ORDER BY user_rank;


SELECT
    us.id,
    us.page,
    us.user_rank,
    us.username,
--   CASE
--     WHEN (SELECT COUNT(*) FROM leetcode_cheater_webscrap.submission s WHERE s.code_hash = s1.code_hash) > 1 THEN 1 ELSE 0
--   END AS question1_copy,
--   CASE
--     WHEN (SELECT COUNT(*) FROM leetcode_cheater_webscrap.submission s WHERE s.code_hash = s2.code_hash) > 1 THEN 1 ELSE 0
--   END AS question2_copy,
--   CASE
--     WHEN (SELECT COUNT(*) FROM leetcode_cheater_webscrap.submission s WHERE s.code_hash = s3.code_hash) > 1 THEN 1 ELSE 0
--   END AS question3_copy,
--   CASE
--     WHEN (SELECT COUNT(*) FROM leetcode_cheater_webscrap.submission s WHERE s.code_hash = s4.code_hash) > 1 THEN 1 ELSE 0
--   END AS question4_copy,
    -- Summing all copy counts for a total between 0 and 4
    (CASE
         WHEN (SELECT COUNT(*) FROM leetcode_cheater_webscrap.submission s WHERE s.code_hash = s1.code_hash) > 1 THEN 1 ELSE 0
         END +
     CASE
         WHEN (SELECT COUNT(*) FROM leetcode_cheater_webscrap.submission s WHERE s.code_hash = s2.code_hash) > 1 THEN 1 ELSE 0
         END +
     CASE
         WHEN (SELECT COUNT(*) FROM leetcode_cheater_webscrap.submission s WHERE s.code_hash = s3.code_hash) > 1 THEN 1 ELSE 0
         END +
     CASE
         WHEN (SELECT COUNT(*) FROM leetcode_cheater_webscrap.submission s WHERE s.code_hash = s4.code_hash) > 1 THEN 1 ELSE 0
         END) AS total_copy_count
FROM leetcode_cheater_webscrap.user_submission us
         LEFT JOIN leetcode_cheater_webscrap.submission s1 ON us.question_one_submission_id = s1.id
         LEFT JOIN leetcode_cheater_webscrap.submission s2 ON us.question_two_submission_id = s2.id
         LEFT JOIN leetcode_cheater_webscrap.submission s3 ON us.question_three_submission_id = s3.id
         LEFT JOIN leetcode_cheater_webscrap.submission s4 ON us.question_four_submission_id = s4.id
HAVING total_copy_count>1
ORDER BY us.user_rank ASC;

-- With ids :
WITH copies AS (SELECT code_hash
                FROM leetcode_cheater_webscrap.submission s
                WHERE code_hash != '47DEQpj8HBSa+/TImW+5JCeuQeRkm5NMpJWZG3hSuFU='
GROUP BY code_hash,question_number
HAVING COUNT(*)>1)
SELECT
    us.id,
    us.page,
    us.user_rank,
    us.username,
    (CASE
         WHEN s1.code_hash IN (SELECT code_hash FROM copies) THEN 1 ELSE 0
         END +
     CASE
         WHEN s2.code_hash IN (SELECT code_hash FROM copies) THEN 1 ELSE 0
         END +
     CASE
         WHEN s3.code_hash IN (SELECT code_hash FROM copies) THEN 1 ELSE 0
         END +
     CASE
         WHEN s4.code_hash IN (SELECT code_hash FROM copies) THEN 1 ELSE 0
         END) AS total_copy_count
FROM leetcode_cheater_webscrap.user_submission us
         LEFT JOIN leetcode_cheater_webscrap.submission s1 ON us.question_one_submission_id = s1.id
         LEFT JOIN leetcode_cheater_webscrap.submission s2 ON us.question_two_submission_id = s2.id
         LEFT JOIN leetcode_cheater_webscrap.submission s3 ON us.question_three_submission_id = s3.id
         LEFT JOIN leetcode_cheater_webscrap.submission s4 ON us.question_four_submission_id = s4.id
HAVING total_copy_count>1
ORDER BY us.user_rank ASC;


-----------------------------------------------

SELECT id FROM leetcode_cheater_webscrap.submission where code_hash="copy_hash";
SELECT * FROM leetcode_cheater_webscrap.submission where id in (0);

SELECT id FROM leetcode_cheater_webscrap.submission where code_hash in (SELECT code_hash FROM leetcode_cheater_webscrap.submission where id in (0));

SELECT code_hash,COUNT(*) AS submission_count, (SELECT MIN(id) from leetcode_cheater_webscrap.submission temp WHERE temp.code_hash=s.code_hash) as min_id
FROM leetcode_cheater_webscrap.submission s
WHERE question_number = 'Q2' AND code_hash != '47DEQpj8HBSa+/TImW+5JCeuQeRkm5NMpJWZG3hSuFU='
GROUP BY code_hash,question_number
HAVING submission_count>1
ORDER BY submission_count DESC;


SELECT code_hash
FROM leetcode_cheater_webscrap.submission s
WHERE code_hash != '47DEQpj8HBSa+/TImW+5JCeuQeRkm5NMpJWZG3hSuFU='
GROUP BY code_hash,question_number
HAVING COUNT(*)>1;
