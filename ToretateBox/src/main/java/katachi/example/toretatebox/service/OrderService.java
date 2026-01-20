package katachi.example.toretatebox.service;

import java.util.ArrayList;
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
     * 注文作成（会員/ゲスト両対応）
     * - 会員: userId を入れる
     * - ゲスト: userId は null
     */
    @Transactional
    public Integer createOrder(List<CartItem> cart, Address address, Integer userId) {

        // 0) 入力チェック
        if (cart == null || cart.isEmpty()) {
            throw new IllegalArgumentException("cart が空です");
        }
        if (address == null) {
            throw new IllegalArgumentException("address が null です");
        }
        if (address.getId() == null) {
            // ここがあなたの「address.getId() == null」問題の本丸
            // = DBに保存済みのAddressを取れていない(またはEntityがidを持てていない)
            throw new IllegalArgumentException("address の id が null です（DBから取得できていません）");
        }

        // 1) 合計金額
        int totalAmount = calcTotalAmount(cart);

        // 2) orders 保存（orderId 発行）
        Order order = new Order();
        order.setUserId(userId);              // ゲストは null OK（DBがNULL許可ならOK）
        order.setAddressId(address.getId());  // 住所IDは必須
        order.setTotalAmount(totalAmount);

        Order savedOrder = orderRepository.save(order);

        Integer orderId = savedOrder.getId();
        if (orderId == null) {
            throw new IllegalStateException("orderId の発行に失敗しました（orders保存後にidが取れません）");
        }

        // 3) order_items 保存
        List<OrderItem> items = new ArrayList<>();
        for (CartItem c : cart) {

            if (c.getProductId() == null) {
                throw new IllegalArgumentException("productId が null の商品があります");
            }
            if (c.getQuantity() <= 0) {
                throw new IllegalArgumentException("quantity が 0 以下の商品があります");
            }

            OrderItem oi = new OrderItem();
            oi.setOrderId(orderId);
            oi.setProductId(c.getProductId());
            oi.setQuantity(c.getQuantity());
            oi.setUnitPrice(c.getPrice());

            items.add(oi);
        }

        orderItemRepository.saveAll(items);

        return orderId;
    }

    private int calcTotalAmount(List<CartItem> cart) {
        int total = 0;
        for (CartItem item : cart) {
            total += item.getPrice() * item.getQuantity();
        }
        return total;
    }
}
