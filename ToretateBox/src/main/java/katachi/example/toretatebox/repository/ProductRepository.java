package katachi.example.toretatebox.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import katachi.example.toretatebox.domain.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
	List<Product> findAll();
	
	List<Product> findByNameContaining(String keyword);
}
