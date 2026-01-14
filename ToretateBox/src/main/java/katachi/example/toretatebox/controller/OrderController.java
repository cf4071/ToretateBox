package katachi.example.toretatebox.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
import katachi.example.toretatebox.domain.model.Address;
// ↓あなたのクラス名/パッケージに合わせて変更してください
import katachi.example.toretatebox.domain.model.CartItem;
import katachi.example.toretatebox.service.OrderService;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    /**
     * レジ画面表示（templates/user/register.html）
     * GET /order/register
     */
    @GetMapping("/order/register")
    public String showRegister(HttpSession session, Model model, RedirectAttributes ra) {

        @SuppressWarnings("unchecked")
        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
        Address address = (Address) session.getAttribute("address"); // セッションキーが違うならここを変更

        // カートが空ならカートへ
        if (cart == null || cart.isEmpty()) {
            ra.addFlashAttribute("error", "カートが空です。");
            return "redirect:/cart";
        }

        // 住所が無いならゲスト情報入力へ（URLはあなたの画面に合わせて変更）
        if (address == null) {
            ra.addFlashAttribute("error", "住所情報がありません。ゲスト情報を入力してください。");
            return "redirect:/guest";
        }

        int totalAmount = calcTotalAmount(cart);
        int totalCount  = calcTotalCount(cart);

        // register.html が参照している変数名に合わせる
        model.addAttribute("cart", cart);
        model.addAttribute("address", address);
        model.addAttribute("totalAmount", totalAmount);
        model.addAttribute("totalCount", totalCount);

        // ✅ templates/user/register.html
        return "user/register";
    }

    /**
     * 注文確定（register.html のボタン）
     * <form th:action="@{/order/confirm}" method="post">
     */
    @PostMapping("/order/confirm")
    public String confirmOrder(HttpSession session, RedirectAttributes ra) {

        @SuppressWarnings("unchecked")
        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
        Address address = (Address) session.getAttribute("address");

        if (cart == null || cart.isEmpty()) {
            ra.addFlashAttribute("error", "カートが空です。");
            return "redirect:/cart";
        }
        if (address == null) {
            ra.addFlashAttribute("error", "住所情報がありません。ゲスト情報を入力してください。");
            return "redirect:/guest";
        }

        // ✅ 注文をDBに保存して注文番号を作る
        Integer orderId = orderService.createOrder(cart, address);

        // ✅ カートを空にする（確定後）
        session.removeAttribute("cart");

        // 完了画面に注文番号を渡す
        ra.addFlashAttribute("orderId", orderId);

        // POSTのあとにリダイレクト（更新で二重注文になりにくい）
        return "redirect:/order/complete";
    }

    /**
     * 注文完了画面（templates/user/complete.html）
     * GET /order/complete
     */
    @GetMapping("/order/complete")
    public String showComplete() {
        // ✅ templates/user/complete.html
        return "user/complete";
    }

    // ====== 合計計算 ======
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
