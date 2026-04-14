package katachi.example.toretatebox.service;

import java.util.List;

import katachi.example.toretatebox.domain.model.Product;

public interface CartService {

    void addProduct(Integer productId);

    void removeProduct(Integer productId);

    List<Product> getCartItems();

    int getTotalPrice();
}