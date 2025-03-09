package rw.dsacco.clat.repositories;

import rw.dsacco.clat.models.AssessmentQuestions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AssessmentQuestionsRepository extends JpaRepository<AssessmentQuestions, Long> {
    Optional<AssessmentQuestions> findByCode(String code);

    @Query("SELECT q FROM AssessmentQuestions q WHERE q.activity.code = :activityCode")
    List<AssessmentQuestions> findByActivityCode(@Param("activityCode") String activityCode);

    @Query("SELECT q FROM AssessmentQuestions q WHERE " +
            "LOWER(q.enQuestion) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(q.frQuestion) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(q.knQuestion) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<AssessmentQuestions> searchQuestions(@Param("keyword") String keyword);
}
