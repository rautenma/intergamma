package nl.intergamma.poc.Repositories;

import nl.intergamma.poc.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findAll();

    @Transactional
    @Modifying
    @Query("UPDATE Product p set p.available = p.available + 1 WHERE p.id = :id")
    void incrementAvailableCount(Long id);

    @Transactional
    @Modifying
    @Query("UPDATE Product p set p.available = p.available - 1 WHERE p.id = :id")
    void decrementAvailableCount(Long id);

    @Transactional
    @Modifying
    @Query("UPDATE Product p set p.reserved = p.reserved + 1 WHERE p.id = :id")
    void incrementReservedCount(Long id);

    @Transactional
    @Modifying
    @Query("UPDATE Product p set p.reserved = p.reserved - 1 WHERE p.id = :id")
    void decrementReservedCount(Long id);
}
