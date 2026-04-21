package katachi.example.toretatebox.controller.admin;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import katachi.example.toretatebox.service.AdminOrderService;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminOrderController {

    private final AdminOrderService adminOrderService;

    @GetMapping("/orders")
    public String orders(
            @RequestParam(name = "page", defaultValue = "0") int page,
            Model model) {

        if (page < 0) {
            page = 0;
        }

        var orderPage = getOrderPage(page);

        if (page >= orderPage.getTotalPages() && orderPage.getTotalPages() > 0) {
            page = orderPage.getTotalPages() - 1;
            orderPage = getOrderPage(page);
        }

        model.addAttribute("orders", orderPage.getContent());
        model.addAttribute("page", orderPage);

        return "admin/orders";
    }

    @GetMapping("/orders/{id}")
    public String detail(@PathVariable Integer id, Model model) {
        var order = adminOrderService.getOrderDetail(id);
        model.addAttribute("order", order);
        return "admin/order_detail";
    }

    private org.springframework.data.domain.Page<?> getOrderPage(int page) {
        var pageable = PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "createdAt"));
        return adminOrderService.findAdminOrderRows(pageable);
    }
}