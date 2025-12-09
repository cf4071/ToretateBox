package katachi.example.toretatebox.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import katachi.example.toretatebox.domain.model.Product;

@Repository
public interface ProductsRepository extends JpaRepository<Product, Integer> {

    // ✅ 商品をすべて取得（※ JpaRepository に元からあるので実は省略可）
    List<Product> findAll();

    // ✅ 商品名で「あいまい検索」
    // 例：「りん」→「りんご」「青りんご」
    List<Product> findByNameContaining(String keyword);

    // ✅ 季節（旬）で検索 ＋ ページング対応
    Page<Product> findBySeason(String season, Pageable pageable);

    // ✅（必要なら）季節 ＋ 有効商品のみ検索
    // Page<Product> findBySeasonAndIsActive(String season, Boolean isActive, Pageable pageable);
}
