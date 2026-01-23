package katachi.example.toretatebox.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import katachi.example.toretatebox.domain.model.Order;
import katachi.example.toretatebox.domain.model.User;
import katachi.example.toretatebox.repository.OrderRepository;
import katachi.example.toretatebox.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class PurchaseHistoryController {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    @GetMapping("/Purchase_history") 
    public String history(Model model, Principal principal) {


        String email = principal.getName();
        User user = userRepository.findByEmail(email);

        List<Order> orders = orderRepository.findByUserIdOrderByCreatedAtDesc(user.getId());

        model.addAttribute("orders", orders);
        return "user/purchase_history"; // ✅ 先頭の / は付けない
    }
}
