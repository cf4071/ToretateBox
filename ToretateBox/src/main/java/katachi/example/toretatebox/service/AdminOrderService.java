package katachi.example.toretatebox.service;

import java.util.List;

import org.springframework.stereotype.Service;

import katachi.example.toretatebox.domain.model.Order;
import katachi.example.toretatebox.repository.OrderRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminOrderService {

    private final OrderRepository orderRepository;

    public List<Order> findAllOrders() {
        return orderRepository.findAll();
    }
}
