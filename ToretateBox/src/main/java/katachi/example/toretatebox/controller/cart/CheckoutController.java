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

    /**
     * ゲスト情報を受け取り、住所を保存してレジ画面へ進む
     */
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

        // ゲスト住所を保存
        Address savedAddress = addressService.save(address);

        // セッションに保存
        session.setAttribute("guestAddressId", savedAddress.getId());

        // GET /checkout へリダイレクト
        return "redirect:/checkout";
    }

    /**
     * レジ画面表示
     */
    @GetMapping("/checkout")
    public String showCheckout(Authentication auth, HttpSession session, Model model) {

        List<CartItem> cart = getCart(session);

        // カートが空ならカート画面へ戻す
        if (cart.isEmpty()) {
            return "redirect:/cart";
        }

        // 合計情報をセット
        model.addAttribute("cart", cart);
        model.addAttribute("totalAmount", calculateTotalAmount(cart));
        model.addAttribute("totalCount", calculateTotalCount(cart));

        // 未ログイン（ゲスト）の場合
        if (auth == null || auth instanceof AnonymousAuthenticationToken) {
            Integer addressId = (Integer) session.getAttribute("guestAddressId");

            // ゲスト住所がなければゲスト情報登録画面へ
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

        // ユーザーが見つからなくても画面が落ちないようにする
        if (user == null) {
            model.addAttribute("address", null);
            return "cart/checkout";
        }

        // 最新住所を取得
        Address address = addressService.findLatestByUserId(user.getId());
        model.addAttribute("address", address);

        return "cart/checkout";
    }

    /**
     * セッションからカートを取得
     */
    @SuppressWarnings("unchecked")
    private List<CartItem> getCart(HttpSession session) {
        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
        return (cart == null) ? List.of() : cart;
    }

    /**
     * 合計金額
     */
    private int calculateTotalAmount(List<CartItem> cart) {
        int total = 0;
        for (CartItem item : cart) {
            total += item.getSubtotal();
        }
        return total;
    }

    /**
     * 合計点数
     */
    private int calculateTotalCount(List<CartItem> cart) {
        int count = 0;
        for (CartItem item : cart) {
            count += item.getQuantity();
        }
        return count;
    }
}