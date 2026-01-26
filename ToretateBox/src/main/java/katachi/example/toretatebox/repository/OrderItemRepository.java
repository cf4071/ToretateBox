package katachi.example.toretatebox.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import katachi.example.toretatebox.domain.model.OrderItem;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Integer> {

    // ✅ 注文の明細を取る（履歴詳細で使う）
	List<OrderItem> findByOrderIdIn(List<Integer> orderIds);
}
