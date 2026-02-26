package katachi.example.toretatebox.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import katachi.example.toretatebox.domain.model.Product;
import katachi.example.toretatebox.repository.ProductsRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductsService {

    private final ProductsRepository productsRepository;


    public List<Product> getAllProducts() {
        return productsRepository.findAll();
    }


    public Page<Product> findAllPage(Pageable pageable) {
        return productsRepository.findAll(pageable);
    }

    public Product findById(Integer id) {
        return productsRepository.findById(id).orElse(null);
    }

    public Optional<Product> getProductById(Integer id) {
        return productsRepository.findById(id);
    }

    public Product save(Product product) {
        return productsRepository.save(product);
    }

    public void deleteById(Integer id) {
        productsRepository.deleteById(id);
    }

    public Page<Product> searchProducts(String keyword, Pageable pageable) {

        if (keyword == null || keyword.isBlank()) {
            return Page.empty(pageable);
        }

        return productsRepository.findByNameContaining(keyword, pageable);
    }

    public Page<Product> searchBySeason(String season, Pageable pageable) {
        return productsRepository.findBySeason(season, pageable);
    }

    public Page<Product> searchByCategory(Integer categoryId, Pageable pageable) {
        return productsRepository.findByCategoryId(categoryId, pageable);
    }

    public Page<Product> searchByCategoryAndSeason(
            Integer categoryId,
            String season,
            Pageable pageable) {

        return productsRepository.findByCategoryIdAndSeason(categoryId, season, pageable);
    }
}
