package katachi.example.toretatebox.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import katachi.example.toretatebox.domain.model.Address;
import katachi.example.toretatebox.domain.model.CartItem;
import katachi.example.toretatebox.domain.model.Order;
import katachi.example.toretatebox.domain.model.OrderItem;
import katachi.example.toretatebox.repository.OrderItemRepository;
import katachi.example.toretatebox.repository.OrderRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    /**
     * 注文を作成してDB保存（C-6 注文確定）
     * @return 注文番号（orders.id）
     */
    @Transactional
    public Integer createOrder(List<CartItem> cart, Address address) {

        // ① 合計金額計算（CartItemのgetSubtotalを使うと簡単）
        int totalAmount = 0;
        for (CartItem item : cart) {
            totalAmount += item.getSubtotal();
        }

        // ② orders 登録
        Order order = new Order();
        order.setTotalAmount(totalAmount);

        // お届け先情報（Address → Orderへコピー）
        order.setRecipient(address.getRecipient());
        order.setPhoneNumber(address.getPhoneNumber());
        order.setPostalCode(address.getPostalCode());
        order.setPrefecture(address.getPrefecture());
        order.setCity(address.getCity());
        order.setAddressLine1(address.getAddressLine1());
        order.setAddressLine2(address.getAddressLine2());

        // 保存（ここで注文番号が決まる）
        Order savedOrder = orderRepository.save(order);

        // ③ order_items 登録
        for (CartItem item : cart) {

            OrderItem oi = new OrderItem();

            // 注文と紐づけ
            oi.setOrder(savedOrder);

            // 商品情報
            oi.setProductId(item.getProductId());
            oi.setProductName(item.getName()); // ✅ CartItemは name
            oi.setPrice(item.getPrice());
            oi.setQuantity(item.getQuantity());

            orderItemRepository.save(oi);
        }

        // ④ 注文番号を返す
        return savedOrder.getId();
    }
}
