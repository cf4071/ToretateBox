package katachi.example.toretatebox.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import katachi.example.toretatebox.domain.dto.AdminOrderRow;
import katachi.example.toretatebox.domain.model.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {

    // ユーザーの注文履歴（新しい順）
    List<Order> findByUserIdOrderByCreatedAtDesc(Integer userId);

    // ユーザーの注文履歴（ページング + 新しい順）
    Page<Order> findByUserIdOrderByCreatedAtDesc(Integer userId, Pageable pageable);

    /**
     * 管理者：注文一覧用（DTOで軽く取得）
     * Order.user（ManyToOne）を使って JOIN するのが安全
     */
    @Query("""
        SELECT new katachi.example.toretatebox.domain.dto.AdminOrderRow(
            o.id,
            o.userId,
            u.email,
            o.createdAt,
            o.totalAmount
        )
        FROM Order o
        LEFT JOIN o.user u
        ORDER BY o.createdAt DESC
    """)
    Page<AdminOrderRow> findAdminOrderRows(Pageable pageable);
}
