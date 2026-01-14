package katachi.example.toretatebox.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import katachi.example.toretatebox.domain.model.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
}
