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

    private final List<Product> cartItems = new ArrayList<>();

    public void addProduct(Integer productId) {
        Product product = productsService.findById(productId);
        if (product != null) {
            cartItems.add(product);
        }
    }

    public void removeProduct(Integer productId) {
        cartItems.removeIf(p -> p.getId().equals(productId));
    }

    public List<Product> getCartItems() {
        return new ArrayList<>(cartItems);
    }

    public int getTotalPrice() {
        return cartItems.stream().mapToInt(Product::getPrice).sum();
    }
}
