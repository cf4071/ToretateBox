package katachi.example.toretatebox.service;

import java.util.List;

import org.springframework.security.core.Authentication;

import jakarta.servlet.http.HttpSession;
import katachi.example.toretatebox.domain.model.Cart;

public interface CartService {

    List<Cart> getCart(HttpSession session, Authentication authentication);

    void addToCart(HttpSession session, Authentication authentication, Integer productId, int quantity);

    void updateQuantity(HttpSession session, Authentication authentication, Integer productId, int quantity);

    void removeFromCart(HttpSession session, Authentication authentication, Integer productId);

    void clearCart(HttpSession session, Authentication authentication);

    void mergeSessionCartToDb(HttpSession session, Authentication authentication);

    int calculateTotal(List<Cart> cart);

    int calculateTotalQuantity(List<Cart> cart);
}