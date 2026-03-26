package katachi.example.toretatebox.controller.cart;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

    @GetMapping
    public String showCart(HttpSession session, Model model) {
        List<CartItem> cart = getCart(session);
        int totalAmount = calculateTotal(cart);

        model.addAttribute("cart", cart);
        model.addAttribute("totalAmount", totalAmount);

        return "cart/cart";
    }

    @PostMapping("/add")
    public String addToCart(
            @RequestParam Integer productId,
            @RequestParam int quantity,
            HttpSession session) {

        if (quantity < 1) {
            quantity = 1;
        }

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

    @PostMapping("/update")
    public String updateCartQuantity(
            @RequestParam Integer productId,
            @RequestParam(required = false) Integer quantity,
            HttpSession session) {

        if (quantity == null || quantity < 1) {
            quantity = 1;
        }

        List<CartItem> cart = getCart(session);

        for (CartItem item : cart) {
            if (item.getProductId().equals(productId)) {
                item.setQuantity(quantity);
                break;
            }
        }

        session.setAttribute("cart", cart);
        return "redirect:/cart";
    }

    @PostMapping("/remove")
    public String removeFromCart(
            @RequestParam Integer productId,
            HttpSession session) {

        List<CartItem> cart = getCart(session);

        cart.removeIf(item -> item.getProductId().equals(productId));

        session.setAttribute("cart", cart);
        return "redirect:/cart";
    }

    @PostMapping("/clear")
    public String clearCart(HttpSession session) {
        session.removeAttribute("cart");
        return "redirect:/cart";
    }

    @PostMapping("/checkout")
    public String proceedToCheckout(
            Authentication authentication,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        List<CartItem> cart = getCart(session);

        if (cart == null || cart.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "カートに商品がありません。");
            return "redirect:/cart";
        }

        if (authentication == null
                || !authentication.isAuthenticated()
                || "anonymousUser".equals(authentication.getPrincipal())) {

            redirectAttributes.addFlashAttribute("errorMessage",
                    "ログインまたはユーザー登録してください。");
            return "redirect:/cart";
        }

        return "redirect:/checkout";
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