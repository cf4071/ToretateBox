package katachi.example.toretatebox.controller.cart;

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
import katachi.example.toretatebox.service.CartService;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @GetMapping
    public String showCart(HttpSession session, Model model, Authentication authentication) {
        List<CartItem> cart = cartService.getCart(session);
        int totalAmount = cartService.calculateTotal(cart);
        int totalQuantity = cartService.calculateTotalQuantity(cart);

        boolean isLoggedIn = authentication != null
                && authentication.isAuthenticated()
                && !"anonymousUser".equals(authentication.getPrincipal());

        model.addAttribute("cartItems", cart);
        model.addAttribute("totalAmount", totalAmount);
        model.addAttribute("totalQuantity", totalQuantity);
        model.addAttribute("isLoggedIn", isLoggedIn);

        return "cart/cart";
    }

    @PostMapping("/add")
    public String addToCart(
            @RequestParam Integer productId,
            @RequestParam(defaultValue = "1") int quantity,
            HttpSession session) {

        cartService.addToCart(session, productId, quantity);
        return "redirect:/cart";
    }

    @PostMapping("/update")
    public String updateCartQuantity(
            @RequestParam Integer productId,
            @RequestParam(required = false) Integer quantity,
            HttpSession session) {

        cartService.updateQuantity(session, productId, quantity == null ? 1 : quantity);
        return "redirect:/cart";
    }

    @PostMapping("/remove")
    public String removeFromCart(
            @RequestParam Integer productId,
            HttpSession session) {

        cartService.removeFromCart(session, productId);
        return "redirect:/cart";
    }

    @PostMapping("/clear")
    public String clearCart(HttpSession session) {
        cartService.clearCart(session);
        return "redirect:/cart";
    }

    @PostMapping("/checkout")
    public String proceedToCheckout(
            Authentication authentication,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        List<CartItem> cart = cartService.getCart(session);

        if (cart.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "カートに商品がありません。");
            return "redirect:/cart";
        }

        if (authentication == null
                || !authentication.isAuthenticated()
                || "anonymousUser".equals(authentication.getPrincipal())) {

            redirectAttributes.addFlashAttribute("errorMessage",
                    "ログインまたはゲスト情報登録をしてください。");
            return "redirect:/cart";
        }

        return "redirect:/checkout";
    }
}