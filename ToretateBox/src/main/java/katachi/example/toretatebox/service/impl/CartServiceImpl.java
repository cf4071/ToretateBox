package katachi.example.toretatebox.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import katachi.example.toretatebox.domain.model.Product;
import katachi.example.toretatebox.service.CartService;
import katachi.example.toretatebox.service.ProductsService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final ProductsService productsService;

    private final List<Product> cartItems = new ArrayList<>();

    @Override
    public void addProduct(Integer productId) {
        Product product = productsService.findById(productId);
        if (product != null) {
            cartItems.add(product);
        }
    }

    @Override
    public void removeProduct(Integer productId) {
        cartItems.removeIf(p -> p.getId().equals(productId));
    }

    @Override
    public List<Product> getCartItems() {
        return new ArrayList<>(cartItems);
    }

    @Override
    public int getTotalPrice() {
        return cartItems.stream().mapToInt(Product::getPrice).sum();
    }
}