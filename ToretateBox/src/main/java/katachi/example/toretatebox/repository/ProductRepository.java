package katachi.example.toretatebox.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import katachi.example.toretatebox.domain.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    // 既存（削除しなくてOK）
    List<Product> findAll();

    // 既存（部分一致検索）
    List<Product> findByNameContaining(String keyword);

    // ★ 追加：季節（旬）で検索 ＋ ページング対応
    Page<Product> findBySeason(String season, Pageable pageable);

    // ★（任意）active 商品のみ季節検索したい場合はこちら
    // Page<Product> findBySeasonAndIsActive(String season, Boolean isActive, Pageable pageable);
}
