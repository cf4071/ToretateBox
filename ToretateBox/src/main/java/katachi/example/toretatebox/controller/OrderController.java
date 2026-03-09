package katachi.example.toretatebox.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
import katachi.example.toretatebox.domain.model.Address;
import katachi.example.toretatebox.domain.model.CartItem;
import katachi.example.toretatebox.domain.model.User;
import katachi.example.toretatebox.repository.UserRepository;
import katachi.example.toretatebox.service.AddressService;
import katachi.example.toretatebox.service.OrderService;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final AddressService addressService;
    private final UserRepository userRepository;

    @PostMapping("/order/confirm")
    public String confirmOrder(
            HttpSession session,
            RedirectAttributes ra,
            Principal principal
    ) {
        @SuppressWarnings("unchecked")
        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");

        if (cart == null || cart.isEmpty()) {
            ra.addFlashAttribute("error", "カートが空です。");
            return "redirect:/cart";
        }

        Address address = resolveAddress(session, ra, principal);
        if (address == null) {
            return "redirect:/guest";
        }

        Integer userId = resolveUserId(principal);

        Integer orderId = orderService.createOrder(cart, address, userId);

        session.removeAttribute("cart");

        if (principal == null) {
            session.removeAttribute("guestAddressId");
            session.removeAttribute("guestForm");
        }

        ra.addFlashAttribute("orderId", orderId);
        return "redirect:/order/complete";
    }

    @GetMapping("/order/complete")
    public String showComplete() {
        return "order/complete";
    }

    private Address resolveAddress(HttpSession session, RedirectAttributes ra, Principal principal) {
        if (principal != null) {
            Integer userId = resolveUserId(principal);
            Address address = addressService.findLatestByUserId(userId);

            if (address == null) {
                ra.addFlashAttribute("error", "住所情報がありません。住所を登録してください。");
                return null;
            }

            return address;
        }

        Integer guestAddressId = (Integer) session.getAttribute("guestAddressId");
        if (guestAddressId == null) {
            ra.addFlashAttribute("error", "住所情報がありません。ゲスト情報を入力してください。");
            return null;
        }

        Address address = addressService.findById(guestAddressId);
        if (address == null) {
            ra.addFlashAttribute("error", "住所情報が見つかりませんでした。もう一度入力してください。");
            return null;
        }

        return address;
    }

    private Integer resolveUserId(Principal principal) {
        if (principal == null) {
            return 1;
        }

        String email = principal.getName();
        User user = userRepository.findByEmail(email);

        if (user == null) {
            return 1;
        }

        return user.getId();
    }
}