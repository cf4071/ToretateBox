package katachi.example.toretatebox.controller;

import java.util.List;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpSession;
import katachi.example.toretatebox.domain.model.Address;
import katachi.example.toretatebox.domain.model.CartItem;
import katachi.example.toretatebox.domain.model.User;
import katachi.example.toretatebox.repository.UserRepository;
import katachi.example.toretatebox.service.AddressService;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class RegisterController {

    private final AddressService addressService;
    private final UserRepository userRepository;

    @GetMapping("/register")
    public String showRegister(Authentication auth, HttpSession session, Model model) {

        List<CartItem> cart = getCart(session);

        if (cart.isEmpty()) {
            return "redirect:/cart";
        }

        int totalAmount = calculateTotalAmount(cart);
        int totalCount = calculateTotalCount(cart);

        model.addAttribute("cart", cart);
        model.addAttribute("totalAmount", totalAmount);
        model.addAttribute("totalCount", totalCount);

        if (auth == null || auth instanceof AnonymousAuthenticationToken) {

            Integer addressId = (Integer) session.getAttribute("guestAddressId");
            if (addressId == null) {
                return "redirect:/guest";
            }

            Address address = addressService.findById(addressId);
            model.addAttribute("address", address);

            return "user/register";
        }

        String email = auth.getName();
        User user = userRepository.findByEmail(email);

        if (user != null) {
            Address address = addressService.findLatestByUserId(user.getId());
            model.addAttribute("address", address);
        }

        return "user/register";
    }

    @SuppressWarnings("unchecked")
    private List<CartItem> getCart(HttpSession session) {
        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
        return (cart == null) ? List.of() : cart;
    }

    private int calculateTotalAmount(List<CartItem> cart) {
        int total = 0;
        for (CartItem item : cart) {
            total += item.getSubtotal();
        }
        return total;
    }

    private int calculateTotalCount(List<CartItem> cart) {
        int count = 0;
        for (CartItem item : cart) {
            count += item.getQuantity();
        }
        return count;
    }
}
