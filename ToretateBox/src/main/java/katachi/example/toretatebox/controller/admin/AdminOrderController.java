package katachi.example.toretatebox.controller.admin;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import katachi.example.toretatebox.domain.dto.AdminOrderRow;
import katachi.example.toretatebox.repository.OrderRepository;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class AdminOrderController {

    private final OrderRepository orderRepository;

    @GetMapping("/admin/orders")
    public String orders(@RequestParam(name="page", defaultValue="0") int page,
                         Model model) {

        int size = 10;

        Page<AdminOrderRow> orderPage = orderRepository.findAdminOrderRows(
            PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"))
        );

        model.addAttribute("page", orderPage);
        model.addAttribute("orders", orderPage.getContent());

        return "admin/orders";
    }
}
