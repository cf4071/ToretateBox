package katachi.example.toretatebox.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import katachi.example.toretatebox.domain.model.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {

    // ✅ ログインユーザーの注文一覧（新しい順）
    List<Order> findByUserIdOrderByCreatedAtDesc(Integer userId);
}
