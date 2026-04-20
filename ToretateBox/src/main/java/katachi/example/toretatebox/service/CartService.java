package katachi.example.toretatebox.service;

import java.util.List;

import jakarta.servlet.http.HttpSession;
import katachi.example.toretatebox.domain.model.CartItem;

public interface CartService {

    List<CartItem> getCart(HttpSession session);

    void addToCart(HttpSession session, Integer productId, int quantity);

    void updateQuantity(HttpSession session, Integer productId, int quantity);

    void removeFromCart(HttpSession session, Integer productId);

    void clearCart(HttpSession session);

    int calculateTotal(List<CartItem> cart);

    int calculateTotalQuantity(List<CartItem> cart);
}