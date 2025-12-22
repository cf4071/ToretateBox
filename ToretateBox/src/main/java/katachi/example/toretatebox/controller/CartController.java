package katachi.example.toretatebox.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;
import katachi.example.toretatebox.domain.model.CartItem;
import katachi.example.toretatebox.domain.model.Product;
import katachi.example.toretatebox.service.ProductsService;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {

    private final ProductsService productsService;

    // =========================
    // カート表示
    // =========================
    @GetMapping
    public String showCart(HttpSession session, Model model) {

        @SuppressWarnings("unchecked")
        List<CartItem> cart =
            (List<CartItem>) session.getAttribute("cart");

        if (cart == null) {
            cart = new ArrayList<>();
        }

        // ▼ 合計金額を計算
        int totalAmount = 0;
        for (CartItem item : cart) {
            totalAmount += item.getSubtotal();
        }

        model.addAttribute("cart", cart);
        model.addAttribute("totalAmount", totalAmount);

        return "user/cart";
    }

    // =========================
    // カートに追加（数量つき）
    // =========================
    @PostMapping("/add")
    public String addToCart(
            @RequestParam Integer productId,
            @RequestParam int quantity,
            HttpSession session) {

        Product product = productsService.findById(productId);
        if (product == null) {
            return "redirect:/products";
        }

        @SuppressWarnings("unchecked")
        List<CartItem> cart =
            (List<CartItem>) session.getAttribute("cart");

        if (cart == null) {
            cart = new ArrayList<>();
        }

        // ▼ すでに同じ商品があるかチェック
        for (CartItem item : cart) {
            if (item.getProductId().equals(productId)) {
                // あれば数量を足す
                item.setQuantity(item.getQuantity() + quantity);
                session.setAttribute("cart", cart);
                return "redirect:/cart";
            }
        }

        // ▼ なければ新規追加
        CartItem newItem = new CartItem();
        newItem.setProductId(product.getId());
        newItem.setName(product.getName());
        newItem.setPrice(product.getPrice());
        newItem.setQuantity(quantity);

        // ★ 商品画像をセット
        newItem.setImageUrl(product.getImageUrl());

        cart.add(newItem);
        session.setAttribute("cart", cart);

        return "redirect:/cart";
    }

    // =========================
    // カートから削除
    // =========================
    @PostMapping("/remove")
    public String removeFromCart(
            @RequestParam Integer productId,
            HttpSession session) {

        @SuppressWarnings("unchecked")
        List<CartItem> cart =
            (List<CartItem>) session.getAttribute("cart");

        if (cart != null) {
            cart.removeIf(
                item -> item.getProductId().equals(productId)
            );
            session.setAttribute("cart", cart);
        }

        return "redirect:/cart";
    }

    // =========================
    // カートを空にする
    // =========================
    @PostMapping("/clear")
    public String clearCart(HttpSession session) {
        session.removeAttribute("cart");
        return "redirect:/cart";
    }
}
