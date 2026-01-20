package katachi.example.toretatebox.domain.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name="orders")
@Data
public class Order {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name="user_id", nullable=false)
  private Integer userId;

  @Column(name="address_id", nullable=false)
  private Integer addressId;

  @Column(name="total_amount", nullable=false)
  private int totalAmount;

  @Column(name="created_at", insertable=false, updatable=false)
  private LocalDateTime createdAt;

  @Column(name="updated_at", insertable=false, updatable=false)
  private LocalDateTime updatedAt;
}
