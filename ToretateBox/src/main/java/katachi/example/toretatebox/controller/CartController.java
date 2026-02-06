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

        List<CartItem> cart = getCart(session);

        int totalAmount = calculateTotal(cart);

        model.addAttribute("cart", cart);
        model.addAttribute("totalAmount", totalAmount);

        return "user/cart";
    }


    @PostMapping("/add")
    public String addToCart(
            @RequestParam Integer productId,
            @RequestParam int quantity,
            HttpSession session) {

        Product product = productsService.findById(productId);
        if (product == null) {
            return "redirect:/products";
        }

        List<CartItem> cart = getCart(session);

        for (CartItem item : cart) {
            if (item.getProductId().equals(productId)) {
                item.setQuantity(item.getQuantity() + quantity);
                session.setAttribute("cart", cart);
                return "redirect:/cart";
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

        return "redirect:/cart";
    }


    @PostMapping("/remove")
    public String removeFromCart(
            @RequestParam Integer productId,
            HttpSession session) {

        List<CartItem> cart = getCart(session);

        cart.removeIf(
            item -> item.getProductId().equals(productId)
        );

        session.setAttribute("cart", cart);
        return "redirect:/cart";
    }


    @PostMapping("/clear")
    public String clearCart(HttpSession session) {
        session.removeAttribute("cart");
        return "redirect:/cart";
    }

    @SuppressWarnings("unchecked")
    private List<CartItem> getCart(HttpSession session) {
        List<CartItem> cart =
            (List<CartItem>) session.getAttribute("cart");

        if (cart == null) {
            cart = new ArrayList<>();
        }
        return cart;
    }

    private int calculateTotal(List<CartItem> cart) {
        int total = 0;
        for (CartItem item : cart) {
            total += item.getSubtotal();
        }
        return total;
    }
}
