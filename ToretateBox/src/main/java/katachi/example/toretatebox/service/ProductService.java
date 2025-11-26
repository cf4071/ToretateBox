package katachi.example.toretatebox.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import katachi.example.toretatebox.domain.model.Product;
import katachi.example.toretatebox.repository.ProductRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    // 商品一覧取得
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    // 商品1件取得（id指定）
    public Optional<Product> getProductById(Integer id) {
        return productRepository.findById(id);
    }

    // 商品を保存（新規 or 更新）
    public Product save(Product product) {
        return productRepository.save(product);
    }

    // 商品削除
    public void deleteById(Integer id) {
        productRepository.deleteById(id);
    }
}