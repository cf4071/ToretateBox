package katachi.example.toretatebox.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.servlet.http.HttpSession;
import katachi.example.toretatebox.domain.model.Cart;
import katachi.example.toretatebox.domain.model.CartItem;
import katachi.example.toretatebox.domain.model.Product;
import katachi.example.toretatebox.domain.model.User;
import katachi.example.toretatebox.repository.CartItemRepository;
import katachi.example.toretatebox.repository.UserRepository;
import katachi.example.toretatebox.service.CartService;
import katachi.example.toretatebox.service.ProductsService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class CartServiceImpl implements CartService {

    private static final String CART_SESSION_KEY = "cart";

    private static final int MIN_QUANTITY = 1;
    private static final int MAX_QUANTITY = 10;

    private final ProductsService productsService;
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;

    @Override
    public List<Cart> getCart(HttpSession session, Authentication authentication) {

        if (isLoggedIn(authentication)) {
            return getDbCart(authentication);
        }

        return getSessionCart(session);
    }

    @Override
    public void addToCart(
            HttpSession session,
            Authentication authentication,
            Integer productId,
            int quantity) {

        if (isLoggedIn(authentication)) {
            addToDbCart(authentication, productId, quantity);
            return;
        }

        addToSessionCart(session, productId, quantity);
    }

    @Override
    public void updateQuantity(
            HttpSession session,
            Authentication authentication,
            Integer productId,
            int quantity) {

        if (isLoggedIn(authentication)) {
            updateDbQuantity(authentication, productId, quantity);
            return;
        }

        updateSessionQuantity(session, productId, quantity);
    }

    @Override
    public void removeFromCart(
            HttpSession session,
            Authentication authentication,
            Integer productId) {

        if (isLoggedIn(authentication)) {
            removeFromDbCart(authentication, productId);
            return;
        }

        removeFromSessionCart(session, productId);
    }

    @Override
    public void clearCart(HttpSession session, Authentication authentication) {

        if (isLoggedIn(authentication)) {
            clearDbCart(authentication);
            return;
        }

        clearSessionCart(session);
    }
    
    @Override
    public void mergeSessionCartToDb(HttpSession session, Authentication authentication) {

        if (!isLoggedIn(authentication)) {
            return;
        }

        List<Cart> sessionCart = getSessionCart(session);

        if (sessionCart.isEmpty()) {
            return;
        }

        for (Cart item : sessionCart) {
            addToDbCart(authentication, item.getProductId(), item.getQuantity());
        }

        clearSessionCart(session);
    }

    @Override
    public int calculateTotal(List<Cart> cart) {
        int total = 0;

        for (Cart item : cart) {
            total += item.getSubtotal();
        }

        return total;
    }

    @Override
    public int calculateTotalQuantity(List<Cart> cart) {
        int totalQuantity = 0;

        for (Cart item : cart) {
            totalQuantity += item.getQuantity();
        }

        return totalQuantity;
    }

    private List<Cart> getDbCart(Authentication authentication) {
        User user = getLoginUser(authentication);

        if (user == null) {
            return new ArrayList<>();
        }

        List<CartItem> dbCartItems = cartItemRepository.findByUserId(user.getId());
        List<Cart> cartList = new ArrayList<>();

        for (CartItem dbCartItem : dbCartItems) {
            Product product = productsService.findById(dbCartItem.getProductId());

            if (product == null) {
                continue;
            }

            Cart cart = new Cart();
            cart.setProductId(product.getId());
            cart.setName(product.getName());
            cart.setPrice(product.getPrice());
            cart.setImageUrl(product.getImageUrl());
            cart.setQuantity(dbCartItem.getQuantity());

            cartList.add(cart);
        }

        return cartList;
    }

    private void addToDbCart(Authentication authentication, Integer productId, int quantity) {
        quantity = normalizeQuantity(quantity);

        User user = getLoginUser(authentication);
        if (user == null) {
            return;
        }

        Product product = productsService.findById(productId);
        if (product == null) {
            return;
        }

        CartItem dbCartItem = cartItemRepository
                .findByUserIdAndProductId(user.getId(), productId)
                .orElse(null);

        if (dbCartItem != null) {
            int newQuantity = dbCartItem.getQuantity() + quantity;
            dbCartItem.setQuantity(normalizeQuantity(newQuantity));
            cartItemRepository.save(dbCartItem);
            return;
        }

        CartItem newDbCartItem = new CartItem();
        newDbCartItem.setUserId(user.getId());
        newDbCartItem.setProductId(productId);
        newDbCartItem.setQuantity(quantity);

        cartItemRepository.save(newDbCartItem);
    }

    private void updateDbQuantity(Authentication authentication, Integer productId, int quantity) {
        quantity = normalizeQuantity(quantity);

        User user = getLoginUser(authentication);
        if (user == null) {
            return;
        }

        CartItem dbCartItem = cartItemRepository
                .findByUserIdAndProductId(user.getId(), productId)
                .orElse(null);

        if (dbCartItem == null) {
            return;
        }

        dbCartItem.setQuantity(quantity);
        cartItemRepository.save(dbCartItem);
    }

    private void removeFromDbCart(Authentication authentication, Integer productId) {
        User user = getLoginUser(authentication);
        if (user == null) {
            return;
        }

        cartItemRepository.deleteByUserIdAndProductId(user.getId(), productId);
    }

    private void clearDbCart(Authentication authentication) {
        User user = getLoginUser(authentication);
        if (user == null) {
            return;
        }

        cartItemRepository.deleteByUserId(user.getId());
    }

    @SuppressWarnings("unchecked")
    private List<Cart> getSessionCart(HttpSession session) {
        List<Cart> cart = (List<Cart>) session.getAttribute(CART_SESSION_KEY);

        if (cart == null) {
            cart = new ArrayList<>();
            session.setAttribute(CART_SESSION_KEY, cart);
        }

        return cart;
    }

    private void addToSessionCart(HttpSession session, Integer productId, int quantity) {
        quantity = normalizeQuantity(quantity);

        Product product = productsService.findById(productId);
        if (product == null) {
            return;
        }

        List<Cart> cart = getSessionCart(session);

        for (Cart item : cart) {
            if (item.getProductId().equals(productId)) {
                int newQuantity = item.getQuantity() + quantity;
                item.setQuantity(normalizeQuantity(newQuantity));
                session.setAttribute(CART_SESSION_KEY, cart);
                return;
            }
        }

        Cart newItem = new Cart();
        newItem.setProductId(product.getId());
        newItem.setName(product.getName());
        newItem.setPrice(product.getPrice());
        newItem.setQuantity(quantity);
        newItem.setImageUrl(product.getImageUrl());

        cart.add(newItem);
        session.setAttribute(CART_SESSION_KEY, cart);
    }

    private void updateSessionQuantity(HttpSession session, Integer productId, int quantity) {
        quantity = normalizeQuantity(quantity);

        List<Cart> cart = getSessionCart(session);

        for (Cart item : cart) {
            if (item.getProductId().equals(productId)) {
                item.setQuantity(quantity);
                break;
            }
        }

        session.setAttribute(CART_SESSION_KEY, cart);
    }

    private void removeFromSessionCart(HttpSession session, Integer productId) {
        List<Cart> cart = getSessionCart(session);
        cart.removeIf(item -> item.getProductId().equals(productId));
        session.setAttribute(CART_SESSION_KEY, cart);
    }

    private void clearSessionCart(HttpSession session) {
        session.removeAttribute(CART_SESSION_KEY);
    }

    private User getLoginUser(Authentication authentication) {
        if (!isLoggedIn(authentication)) {
            return null;
        }

        String email = authentication.getName();
        return userRepository.findByEmail(email);
    }

    private boolean isLoggedIn(Authentication authentication) {
        return authentication != null
                && authentication.isAuthenticated()
                && !"anonymousUser".equals(authentication.getPrincipal());
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