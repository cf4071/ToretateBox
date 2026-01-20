package katachi.example.toretatebox.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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

    /**
     * レジ画面表示
     * GET /order/register
     */
    @GetMapping("/order/register")
    public String showRegister(
            HttpSession session,
            Model model,
            RedirectAttributes ra,
            Principal principal
    ) {
        // 1) カート
        @SuppressWarnings("unchecked")
        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
        if (cart == null || cart.isEmpty()) {
            ra.addFlashAttribute("error", "カートが空です。");
            return "redirect:/cart";
        }

        // 2) 住所（会員なら userId から / ゲストなら guestAddressId から）
        Address address = resolveAddress(session, ra, principal);
        if (address == null) {
            // resolveAddress 内で ra にメッセージを入れている
            return "redirect:/guest";
        }

        // 3) 合計
        int totalAmount = calcTotalAmount(cart);
        int totalCount  = calcTotalCount(cart);

        // 4) 画面へ
        model.addAttribute("cart", cart);
        model.addAttribute("address", address);
        model.addAttribute("totalAmount", totalAmount);
        model.addAttribute("totalCount", totalCount);

        // あなたの配置が templates/user/register.html ならこれでOK
        return "user/register";
    }

    /**
     * 注文確定
     * POST /order/confirm
     */
    @PostMapping("/order/confirm")
    public String confirmOrder(
            HttpSession session,
            RedirectAttributes ra,
            Principal principal
    ) {
        // 1) カート
        @SuppressWarnings("unchecked")
        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
        if (cart == null || cart.isEmpty()) {
            ra.addFlashAttribute("error", "カートが空です。");
            return "redirect:/cart";
        }

        // 2) 住所
        Address address = resolveAddress(session, ra, principal);
        if (address == null) {
            return "redirect:/guest";
        }

        // 3) userId（会員なら入れる / ゲストは null）
        Integer userId = resolveUserId(principal);

        // 4) 注文作成（orders + order_items）
        Integer orderId = orderService.createOrder(cart, address, userId);

        // 5) 後片付け：カートを空にする（ゲストは住所IDも消してOK）
        session.removeAttribute("cart");
        // ゲスト購入だった場合だけ、ゲスト住所IDも消しておく（任意）
        if (userId == null) {
            session.removeAttribute("guestAddressId");
        }

        // 6) 完了画面へ（注文番号）
        ra.addFlashAttribute("orderId", orderId);
        return "redirect:/order/complete";
    }

    /**
     * 注文完了画面
     * GET /order/complete
     */
    @GetMapping("/order/complete")
    public String showComplete() {
        // templates/order/complete.html に置くならこれ
        return "order/complete";
    }

    // ========= ここから下は共通処理 =========

    /**
     * 会員/ゲストで住所の取り方を切り替える
     */
    private Address resolveAddress(HttpSession session, RedirectAttributes ra, Principal principal) {

        // 会員（ログインあり）
        Integer userId = resolveUserId(principal);
        if (userId != null) {
            Address address = addressService.findLatestByUserId(userId);
            if (address == null) {
                ra.addFlashAttribute("error", "住所情報がありません。住所を登録してください。");
                return null;
            }
            return address;
        }

        // ゲスト（ログインなし）
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

    /**
     * Principal から userId を取る（取れなければ null）
     */
    private Integer resolveUserId(Principal principal) {
        if (principal == null) return null;

        String email = principal.getName();
        User user = userRepository.findByEmail(email);
        if (user == null) return null;

        return user.getId();
    }

    private int calcTotalAmount(List<CartItem> cart) {
        int total = 0;
        for (CartItem item : cart) {
            total += item.getPrice() * item.getQuantity();
        }
        return total;
    }

    private int calcTotalCount(List<CartItem> cart) {
        int count = 0;
        for (CartItem item : cart) {
            count += item.getQuantity();
        }
        return count;
    }
}
