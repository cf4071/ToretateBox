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
import katachi.example.toretatebox.domain.model.Cart;
import katachi.example.toretatebox.domain.model.User;
import katachi.example.toretatebox.form.GuestForm;
import katachi.example.toretatebox.repository.UserRepository;
import katachi.example.toretatebox.service.AddressService;
import katachi.example.toretatebox.service.CartService;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class CheckoutController {

    private final AddressService addressService;
    private final UserRepository userRepository;
    private final CartService cartService;

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

        Address savedAddress = addressService.save(address);

        session.setAttribute("guestAddressId", savedAddress.getId());

        return "redirect:/checkout";
    }

    @GetMapping("/checkout")
    public String showCheckout(Authentication auth, HttpSession session, Model model) {

        List<Cart> cart = cartService.getCart(session, auth);

        if (cart.isEmpty()) {
            return "redirect:/cart";
        }

        model.addAttribute("cart", cart);
        model.addAttribute("totalAmount", cartService.calculateTotal(cart));
        model.addAttribute("totalCount", cartService.calculateTotalQuantity(cart));

        if (auth == null || auth instanceof AnonymousAuthenticationToken) {
            Integer addressId = (Integer) session.getAttribute("guestAddressId");

            if (addressId == null) {
                return "redirect:/guest";
            }

            Address address = addressService.findById(addressId);
            model.addAttribute("address", address);

            return "cart/checkout";
        }

        String email = auth.getName();
        User user = userRepository.findByEmail(email);

        if (user == null) {
            model.addAttribute("address", null);
            return "cart/checkout";
        }

        Address address = addressService.findLatestByUserId(user.getId());
        model.addAttribute("address", address);

        return "cart/checkout";
    }
}