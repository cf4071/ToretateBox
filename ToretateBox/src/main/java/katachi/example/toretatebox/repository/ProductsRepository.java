package katachi.example.toretatebox.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import katachi.example.toretatebox.domain.model.Product;

@Repository
public interface ProductsRepository extends JpaRepository<Product, Integer> {

    // 商品名あいまい検索（ページング対応）
    Page<Product> findByNameContaining(String keyword, Pageable pageable);

    // 季節（旬）
    Page<Product> findBySeason(String season, Pageable pageable);

    // カテゴリID
    Page<Product> findByCategoryId(Integer categoryId, Pageable pageable);

    // カテゴリID＋季節
    Page<Product> findByCategoryIdAndSeason(
            Integer categoryId,
            String season,
            Pageable pageable);
}
