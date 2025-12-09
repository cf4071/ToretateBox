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

    // ✅ 商品一覧取得
    public List<Product> getAllProducts() {
        return productsRepository.findAll();
    }

    // ✅ 商品1件取得（id指定）
    public Optional<Product> getProductById(Integer id) {
        return productsRepository.findById(id);
    }

    // ✅ 商品を保存（新規 or 更新）
    public Product save(Product product) {
        return productsRepository.save(product);
    }

    // ✅ 商品削除
    public void deleteById(Integer id) {
        productsRepository.deleteById(id);
    }

    // ✅ 商品名で検索（あいまい検索）
    public List<Product> searchProducts(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return List.of();
        }
        return productsRepository.findByNameContaining(keyword);
    }

    // ✅【★追加】季節（旬）で商品をページング検索
    public Page<Product> searchBySeason(String season, Pageable pageable) {
        return productsRepository.findBySeason(season, pageable);
    }
}
