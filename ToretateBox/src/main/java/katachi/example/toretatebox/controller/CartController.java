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
        List<Product> cart = (List<Product>) session.getAttribute("cart");

        if (cart == null) {
            cart = new ArrayList<>();
        }

        model.addAttribute("cart", cart);
        return "user/cart";
    }

    // =========================
    // カートに追加
    // =========================
    @PostMapping("/add")
    public String addToCart(@RequestParam Integer productId, HttpSession session) {
        Product product = productsService.findById(productId);
        if (product == null) {
            return "redirect:/products";
        }

        @SuppressWarnings("unchecked")
        List<Product> cart = (List<Product>) session.getAttribute("cart");
        if (cart == null) {
            cart = new ArrayList<>();
        }

        cart.add(product);
        session.setAttribute("cart", cart);

        return "redirect:/cart"; // カートページへ遷移
    }

    // =========================
    // カートから削除
    // =========================
    @PostMapping("/remove")
    public String removeFromCart(@RequestParam Integer productId, HttpSession session) {
        @SuppressWarnings("unchecked")
        List<Product> cart = (List<Product>) session.getAttribute("cart");
        if (cart != null) {
            cart.removeIf(p -> p.getId().equals(productId));
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
