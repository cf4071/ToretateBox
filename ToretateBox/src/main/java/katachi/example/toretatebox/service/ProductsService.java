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

    // =============================
    // すべて取得（ページング無し）
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
    // 商品IDで1件取得
    // =============================
    public Product findById(Integer id) {
        return productsRepository.findById(id).orElse(null);
    }

    public Optional<Product> getProductById(Integer id) {
        return productsRepository.findById(id);
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
    // キーワード検索（ページングあり）
    // =============================
    public Page<Product> searchProducts(String keyword, Pageable pageable) {

        // keywordが空なら「空のページ」を返す（仕様：空なら検索結果0件）
        if (keyword == null || keyword.isBlank()) {
            return Page.empty(pageable);
        }

        return productsRepository.findByNameContaining(keyword, pageable);
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
