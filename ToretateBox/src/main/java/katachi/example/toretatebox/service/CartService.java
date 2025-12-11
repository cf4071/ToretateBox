package katachi.example.toretatebox.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import katachi.example.toretatebox.domain.model.Product;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CartService {

    private final ProductsService productsService;

    // 仮に簡易的にセッションを使わずメモリ上で保持
    private final List<Product> cartItems = new ArrayList<>();

    // カートに追加
    public void addProduct(Integer productId) {
        Product product = productsService.findById(productId);
        if (product != null) {
            cartItems.add(product);
        }
    }

    // カートから削除
    public void removeProduct(Integer productId) {
        cartItems.removeIf(p -> p.getId().equals(productId));
    }

    // カート内商品一覧取得
    public List<Product> getCartItems() {
        return new ArrayList<>(cartItems);
    }

    // 合計金額
    public int getTotalPrice() {
        return cartItems.stream().mapToInt(Product::getPrice).sum();
    }
}
