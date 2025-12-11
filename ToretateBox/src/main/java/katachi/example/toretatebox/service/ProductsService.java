package katachi.example.toretatebox.service;

import java.util.List;

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

    // =============================
    // すべて取得
    // =============================
    public List<Product> getAllProducts() {
        return productsRepository.findAll();
    }

    // =============================
    // ページング取得
    // =============================
    public Page<Product> findAllPage(Pageable pageable) {
        return productsRepository.findAll(pageable);
    }

    // =============================
    // ★ 商品IDで1件取得（Controller と整合）
    // =============================
    public Product findById(Integer id) {
        return productsRepository.findById(id).orElse(null);
    }

    // =============================
    // 保存
    // =============================
    public Product save(Product product) {
        return productsRepository.save(product);
    }

    // =============================
    // 削除
    // =============================
    public void deleteById(Integer id) {
        productsRepository.deleteById(id);
    }

    // =============================
    // キーワード検索
    // =============================
    public List<Product> searchProducts(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return List.of();
        }
        return productsRepository.findByNameContaining(keyword);
    }

    // =============================
    // 季節検索
    // =============================
    public Page<Product> searchBySeason(String season, Pageable pageable) {
        return productsRepository.findBySeason(season, pageable);
    }

    // =============================
    // カテゴリ検索
    // =============================
    public Page<Product> searchByCategory(Integer categoryId, Pageable pageable) {
        return productsRepository.findByCategoryId(categoryId, pageable);
    }

    // =============================
    // カテゴリ＋季節検索
    // =============================
    public Page<Product> searchByCategoryAndSeason(
            Integer categoryId,
            String season,
            Pageable pageable) {

        return productsRepository.findByCategoryIdAndSeason(categoryId, season, pageable);
    }
}
