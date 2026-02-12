package katachi.example.toretatebox.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import katachi.example.toretatebox.domain.model.Product;

@Repository
public interface ProductsRepository extends JpaRepository<Product, Integer> {

    //商品名あいまい検索
    List<Product> findByNameContaining(String keyword);

    //季節（旬）
    Page<Product> findBySeason(String season, Pageable pageable);

    //カテゴリID
    Page<Product> findByCategoryId(Integer categoryId, Pageable pageable);

    //カテゴリID＋季節
    Page<Product> findByCategoryIdAndSeason(
            Integer categoryId,
            String season,
            Pageable pageable);
}
