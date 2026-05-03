package katachi.example.toretatebox.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpSession;
import katachi.example.toretatebox.domain.model.CartItem;
import katachi.example.toretatebox.domain.model.Product;
import katachi.example.toretatebox.service.CartService;
import katachi.example.toretatebox.service.ProductsService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private static final String CART_SESSION_KEY = "cart";
    private static final int MIN_QUANTITY = 1;
    private static final int MAX_QUANTITY = 10;

    private final ProductsService productsService;

    @Override
    @SuppressWarnings("unchecked")
    public List<CartItem> getCart(HttpSession session) {
        List<CartItem> cart = (List<CartItem>) session.getAttribute(CART_SESSION_KEY);
        if (cart == null) {
            cart = new ArrayList<>();
        }
        return cart;
    }

    @Override
    public void addToCart(HttpSession session, Integer productId, int quantity) {
        quantity = normalizeQuantity(quantity);

        Product product = productsService.findById(productId);
        if (product == null) {
            return;
        }

        List<CartItem> cart = getCart(session);

        for (CartItem item : cart) {
            if (item.getProductId().equals(productId)) {
                int newQuantity = item.getQuantity() + quantity;
                item.setQuantity(normalizeQuantity(newQuantity));
                session.setAttribute(CART_SESSION_KEY, cart);
                return;
            }
        }

        CartItem newItem = new CartItem();
        newItem.setProductId(product.getId());
        newItem.setName(product.getName());
        newItem.setPrice(product.getPrice());
        newItem.setQuantity(quantity);
        newItem.setImageUrl(product.getImageUrl());

        cart.add(newItem);
        session.setAttribute(CART_SESSION_KEY, cart);
    }

    @Override
    public void updateQuantity(HttpSession session, Integer productId, int quantity) {
        quantity = normalizeQuantity(quantity);

        List<CartItem> cart = getCart(session);

        for (CartItem item : cart) {
            if (item.getProductId().equals(productId)) {
                item.setQuantity(quantity);
                break;
            }
        }

        session.setAttribute(CART_SESSION_KEY, cart);
    }

    @Override
    public void removeFromCart(HttpSession session, Integer productId) {
        List<CartItem> cart = getCart(session);
        cart.removeIf(item -> item.getProductId().equals(productId));
        session.setAttribute(CART_SESSION_KEY, cart);
    }

    @Override
    public void clearCart(HttpSession session) {
        session.removeAttribute(CART_SESSION_KEY);
    }

    @Override
    public int calculateTotal(List<CartItem> cart) {
        int total = 0;
        for (CartItem item : cart) {
            total += item.getSubtotal();
        }
        return total;
    }

    @Override
    public int calculateTotalQuantity(List<CartItem> cart) {
        int totalQuantity = 0;
        for (CartItem item : cart) {
            totalQuantity += item.getQuantity();
        }
        return totalQuantity;
    }

    private int normalizeQuantity(int quantity) {
        if (quantity < MIN_QUANTITY) {
            return MIN_QUANTITY;
        }

        if (quantity > MAX_QUANTITY) {
            return MAX_QUANTITY;
        }

        return quantity;
    }
}