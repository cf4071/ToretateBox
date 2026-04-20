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

    private final ProductsService productsService;

    @Override
    @SuppressWarnings("unchecked")
    public List<CartItem> getCart(HttpSession session) {
        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
        if (cart == null) {
            cart = new ArrayList<>();
        }
        return cart;
    }

    @Override
    public void addToCart(HttpSession session, Integer productId, int quantity) {
        if (quantity < 1) {
            quantity = 1;
        }
        if (quantity > 10) {
            quantity = 10;
        }

        Product product = productsService.findById(productId);
        if (product == null) {
            return;
        }

        List<CartItem> cart = getCart(session);

        for (CartItem item : cart) {
            if (item.getProductId().equals(productId)) {
                int newQuantity = item.getQuantity() + quantity;
                if (newQuantity > 10) {
                    newQuantity = 10;
                }
                item.setQuantity(newQuantity);
                session.setAttribute("cart", cart);
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
        session.setAttribute("cart", cart);
    }

    @Override
    public void updateQuantity(HttpSession session, Integer productId, int quantity) {
        if (quantity < 1) {
            quantity = 1;
        }
        if (quantity > 10) {
            quantity = 10;
        }

        List<CartItem> cart = getCart(session);

        for (CartItem item : cart) {
            if (item.getProductId().equals(productId)) {
                item.setQuantity(quantity);
                break;
            }
        }

        session.setAttribute("cart", cart);
    }

    @Override
    public void removeFromCart(HttpSession session, Integer productId) {
        List<CartItem> cart = getCart(session);
        cart.removeIf(item -> item.getProductId().equals(productId));
        session.setAttribute("cart", cart);
    }

    @Override
    public void clearCart(HttpSession session) {
        session.removeAttribute("cart");
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
}