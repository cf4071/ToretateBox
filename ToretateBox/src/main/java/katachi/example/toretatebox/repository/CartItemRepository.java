package katachi.example.toretatebox.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import katachi.example.toretatebox.domain.model.CartItem;

public interface CartItemRepository extends JpaRepository<CartItem, Integer> {

    List<CartItem> findByUserId(Integer userId);

    Optional<CartItem> findByUserIdAndProductId(Integer userId, Integer productId);

    void deleteByUserIdAndProductId(Integer userId, Integer productId);

    void deleteByUserId(Integer userId);
}