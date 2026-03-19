package katachi.example.toretatebox.controller.cart;

import java.util.List;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.servlet.http.HttpSession;
import katachi.example.toretatebox.domain.model.Address;
import katachi.example.toretatebox.domain.model.CartItem;
import katachi.example.toretatebox.domain.model.User;
import katachi.example.toretatebox.form.GuestForm;
import katachi.example.toretatebox.repository.UserRepository;
import katachi.example.toretatebox.service.AddressService;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class CheckoutController {

    private final AddressService addressService;
    private final UserRepository userRepository;

    @PostMapping("/checkout")
    public String submitGuestInfo(@ModelAttribute GuestForm guestForm, HttpSession session) {

        Address address = new Address();
        address.setRecipient(guestForm.getName());
        address.setPhoneNumber(guestForm.getPhoneNumber());
        address.setPostalCode(guestForm.getPostalCode());
        address.setPrefecture(guestForm.getPrefecture());
        address.setCity(guestForm.getCity());
        address.setAddressLine1(guestForm.getAddressLine1());
        address.setAddressLine2(guestForm.getAddressLine2());

        // DBに保存
        Address savedAddress = addressService.save(address);

        // セッションに保存
        session.setAttribute("guestAddressId", savedAddress.getId());

        // レジ画面表示用のGETへ移動
        return "redirect:/checkout";
    }

    @GetMapping("/checkout")
    public String showCheckout(Authentication auth, HttpSession session, Model model) {

        List<CartItem> cart = getCart(session);

        if (cart.isEmpty()) {
            return "redirect:/cart";
        }

        int totalAmount = calculateTotalAmount(cart);
        int totalCount = calculateTotalCount(cart);

        model.addAttribute("cart", cart);
        model.addAttribute("totalAmount", totalAmount);
        model.addAttribute("totalCount", totalCount);

        // 未ログイン（ゲスト）の場合
        if (auth == null || auth instanceof AnonymousAuthenticationToken) {
            Integer addressId = (Integer) session.getAttribute("guestAddressId");

            if (addressId == null) {
                return "redirect:/guest";
            }

            Address address = addressService.findById(addressId);
            model.addAttribute("address", address);

            return "cart/checkout";
        }

        // ログイン済み会員の場合
        String email = auth.getName();
        User user = userRepository.findByEmail(email);

        if (user != null) {
            Address address = addressService.findLatestByUserId(user.getId());
            model.addAttribute("address", address);
        }

        return "cart/checkout";
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