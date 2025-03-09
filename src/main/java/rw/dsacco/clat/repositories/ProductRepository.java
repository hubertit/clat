package rw.dsacco.clat.repositories;

import rw.dsacco.clat.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findById(Long id); // ✅ Ensure this method exists
}