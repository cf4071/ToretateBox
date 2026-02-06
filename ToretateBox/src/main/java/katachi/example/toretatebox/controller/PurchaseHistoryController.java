package katachi.example.toretatebox.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import katachi.example.toretatebox.domain.dto.OrderItemView;
import katachi.example.toretatebox.domain.model.Order;
import katachi.example.toretatebox.domain.model.OrderItem;
import katachi.example.toretatebox.domain.model.Product;
import katachi.example.toretatebox.domain.model.User;
import katachi.example.toretatebox.repository.OrderItemRepository;
import katachi.example.toretatebox.repository.OrderRepository;
import katachi.example.toretatebox.repository.ProductsRepository;
import katachi.example.toretatebox.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class PurchaseHistoryController {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductsRepository productRepository;
    private final UserRepository userRepository;


    @GetMapping("/Purchase_history")
    public String history(
            Model model,
            Principal principal,
            @RequestParam(defaultValue = "0") int page
    ) {

        if (principal == null) {
            return "redirect:/login";
        }

        String email = principal.getName();
        User user = userRepository.findByEmail(email);

        PageRequest pageable = PageRequest.of(page, 8, Sort.by("createdAt").descending());
        Page<Order> orderPage = orderRepository.findByUserIdOrderByCreatedAtDesc(user.getId(), pageable);

        model.addAttribute("orders", orderPage.getContent());
        model.addAttribute("orderPage", orderPage);

        if (orderPage.isEmpty()) {
            return "user/purchase_history";
        }

        List<Integer> orderIds = orderPage.getContent().stream()
                .map(Order::getId)
                .collect(Collectors.toList());

        List<OrderItem> items = orderItemRepository.findByOrderIdIn(orderIds);

        if (items == null || items.isEmpty()) {
            model.addAttribute("orderItemsMap", new HashMap<Integer, List<OrderItemView>>());
            return "user/purchase_history";
        }

        Set<Integer> productIds = items.stream()
                .map(OrderItem::getProductId)
                .collect(Collectors.toSet());

        Map<Integer, String> productNameMap = productRepository.findAllById(productIds).stream()
                .collect(Collectors.toMap(Product::getId, Product::getName));

        Map<Integer, List<OrderItemView>> orderItemsMap = new HashMap<>();
        for (OrderItem it : items) {
            String name = productNameMap.getOrDefault(it.getProductId(), "（商品が見つかりません）");

            OrderItemView view = new OrderItemView(
                    name,
                    it.getQuantity(),
                    it.getUnitPrice()
            );

            orderItemsMap
                    .computeIfAbsent(it.getOrderId(), k -> new ArrayList<>())
                    .add(view);
        }

        model.addAttribute("orderItemsMap", orderItemsMap);
        return "user/purchase_history";
    }
}
