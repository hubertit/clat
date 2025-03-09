package rw.dsacco.clat.repositories;

import rw.dsacco.clat.models.Stages;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.List;

public interface StagesRepository extends JpaRepository<Stages, Long> {
    Optional<Stages> findByCode(String code);
    List<Stages> findByProductId(Long productId);
}
