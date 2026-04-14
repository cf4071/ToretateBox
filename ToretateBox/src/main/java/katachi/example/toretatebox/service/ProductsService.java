package katachi.example.toretatebox.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import katachi.example.toretatebox.domain.model.Product;

public interface ProductsService {

    List<Product> getAllProducts();

    Page<Product> findAllPage(Pageable pageable);

    Product findById(Integer id);

    Optional<Product> getProductById(Integer id);

    Product save(Product product);

    void deleteById(Integer id);

    Page<Product> searchProducts(String keyword, Pageable pageable);

    Page<Product> searchBySeason(String season, Pageable pageable);

    Page<Product> searchByCategory(Integer categoryId, Pageable pageable);

    Page<Product> searchByCategoryAndSeason(Integer categoryId, String season, Pageable pageable);
}