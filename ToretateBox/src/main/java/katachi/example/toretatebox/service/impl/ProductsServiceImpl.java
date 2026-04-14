package katachi.example.toretatebox.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import katachi.example.toretatebox.domain.model.Product;
import katachi.example.toretatebox.repository.ProductsRepository;
import katachi.example.toretatebox.service.ProductsService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductsServiceImpl implements ProductsService {

    private final ProductsRepository productsRepository;

    @Override
    public List<Product> getAllProducts() {
        return productsRepository.findAll();
    }

    @Override
    public Page<Product> findAllPage(Pageable pageable) {
        return productsRepository.findAll(pageable);
    }

    @Override
    public Product findById(Integer id) {
        return productsRepository.findById(id).orElse(null);
    }

    @Override
    public Optional<Product> getProductById(Integer id) {
        return productsRepository.findById(id);
    }

    @Override
    public Product save(Product product) {
        return productsRepository.save(product);
    }

    @Override
    public void deleteById(Integer id) {
        productsRepository.deleteById(id);
    }

    @Override
    public Page<Product> searchProducts(String keyword, Pageable pageable) {
        if (keyword == null || keyword.isBlank()) {
            return Page.empty(pageable);
        }
        return productsRepository.findByNameContaining(keyword, pageable);
    }

    @Override
    public Page<Product> searchBySeason(String season, Pageable pageable) {
        return productsRepository.findBySeason(season, pageable);
    }

    @Override
    public Page<Product> searchByCategory(Integer categoryId, Pageable pageable) {
        return productsRepository.findByCategoryId(categoryId, pageable);
    }

    @Override
    public Page<Product> searchByCategoryAndSeason(
            Integer categoryId,
            String season,
            Pageable pageable) {
        return productsRepository.findByCategoryIdAndSeason(categoryId, season, pageable);
    }
}