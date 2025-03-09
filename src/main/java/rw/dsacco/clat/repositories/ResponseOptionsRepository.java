package rw.dsacco.clat.repositories;

import rw.dsacco.clat.models.ResponseOptions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ResponseOptionsRepository extends JpaRepository<ResponseOptions, Long> {
    Optional<ResponseOptions> findByCode(String code);

    @Query("SELECT o FROM ResponseOptions o WHERE o.question.code = :questionCode")
    List<ResponseOptions> findByQuestionCode(@Param("questionCode") String questionCode);

    @Query("SELECT o FROM ResponseOptions o WHERE " +
            "LOWER(o.enOption) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(o.frOption) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(o.knOption) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<ResponseOptions> searchOptions(@Param("keyword") String keyword);
}

