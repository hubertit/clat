package rw.dsacco.clat.repositories;

import rw.dsacco.clat.models.Products;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ProductsRepository extends JpaRepository<Products, Long> {
    Optional<Products> findByCode(String code);
}
