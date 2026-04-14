package katachi.example.toretatebox.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import katachi.example.toretatebox.domain.dto.AdminOrderDetailDto;
import katachi.example.toretatebox.domain.dto.AdminOrderItemDto;
import katachi.example.toretatebox.domain.dto.AdminOrderRow;
import katachi.example.toretatebox.domain.model.Order;
import katachi.example.toretatebox.domain.model.OrderItem;
import katachi.example.toretatebox.domain.model.User;
import katachi.example.toretatebox.repository.AddressRepository;
import katachi.example.toretatebox.repository.OrderItemRepository;
import katachi.example.toretatebox.repository.OrderRepository;
import katachi.example.toretatebox.repository.ProductsRepository;
import katachi.example.toretatebox.repository.UserRepository;
import katachi.example.toretatebox.service.AdminOrderService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminOrderServiceImpl implements AdminOrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductsRepository productsRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<AdminOrderRow> findAdminOrderRows(Pageable pageable) {
        return orderRepository.findAdminOrderRows(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public AdminOrderDetailDto getOrderDetail(Integer orderId) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("注文が見つかりません: " + orderId));

        User user = order.getUser();
        if (user == null && order.getUserId() != null) {
            user = userRepository.findById(order.getUserId()).orElse(null);
        }

        var address = addressRepository.findById(order.getAddressId())
                .orElseThrow(() -> new IllegalArgumentException("住所が見つかりません: " + order.getAddressId()));

        List<OrderItem> orderItems = orderItemRepository.findByOrderId(orderId);

        var itemDtos = orderItems.stream().map(oi -> {
            var product = productsRepository.findById(oi.getProductId())
                    .orElseThrow(() -> new IllegalArgumentException("商品が見つかりません: " + oi.getProductId()));

            AdminOrderItemDto dto = new AdminOrderItemDto();
            dto.setProductName(product.getName());
            dto.setQuantity(oi.getQuantity());
            dto.setUnitPrice(oi.getUnitPrice());
            dto.setSubtotal(oi.getUnitPrice() * oi.getQuantity());
            return dto;
        }).collect(Collectors.toList());

        AdminOrderDetailDto dto = new AdminOrderDetailDto();
        dto.setOrderId(order.getId().longValue());
        dto.setCreatedAt(order.getCreatedAt());
        dto.setTotalAmount(order.getTotalAmount());

        if (user != null) {
            dto.setCustomerName(user.getName());
            dto.setEmail(user.getEmail());
        } else {
            dto.setCustomerName("");
            dto.setEmail("");
        }

        dto.setPhone(address.getPhoneNumber());
        dto.setPostalCode(address.getPostalCode());
        dto.setPrefecture(address.getPrefecture());
        dto.setCity(address.getCity());
        dto.setAddressLine(address.getAddressLine1());
        dto.setBuilding(address.getAddressLine2());

        dto.setItems(itemDtos);

        return dto;
    }
}