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

    List<Order> findByUserIdOrderByCreatedAtDesc(Integer userId);

    Page<Order> findByUserIdOrderByCreatedAtDesc(Integer userId, Pageable pageable);


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
