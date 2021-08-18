package nl.intergamma.poc.Repositories;

import nl.intergamma.poc.entities.Product;
import nl.intergamma.poc.entities.ReservedProducts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservedProductsRepository extends JpaRepository<ReservedProducts, Long> {
    List<ReservedProducts> findAllByProduct(Product product);
    
}
