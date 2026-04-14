package katachi.example.toretatebox.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import katachi.example.toretatebox.domain.model.Address;
import katachi.example.toretatebox.domain.model.CartItem;
import katachi.example.toretatebox.domain.model.Order;
import katachi.example.toretatebox.domain.model.OrderItem;
import katachi.example.toretatebox.repository.OrderItemRepository;
import katachi.example.toretatebox.repository.OrderRepository;
import katachi.example.toretatebox.service.OrderService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    @Override
    @Transactional
    public Integer createOrder(List<CartItem> cart, Address address, Integer userId) {

        int totalAmount = 0;
        for (CartItem item : cart) {
            totalAmount += item.getPrice() * item.getQuantity();
        }

        Order order = new Order();
        order.setUserId(userId);
        order.setAddressId(address.getId());
        order.setTotalAmount(totalAmount);

        Order saved = orderRepository.save(order);

        for (CartItem item : cart) {
            OrderItem oi = new OrderItem();
            oi.setOrderId(saved.getId());
            oi.setProductId(item.getProductId());
            oi.setQuantity(item.getQuantity());
            oi.setUnitPrice(item.getPrice());

            orderItemRepository.save(oi);
        }

        return saved.getId();
    }
}