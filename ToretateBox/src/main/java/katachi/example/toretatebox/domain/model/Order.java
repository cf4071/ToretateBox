package katachi.example.toretatebox.domain.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // ▼ お届け先情報（注文時点の情報を保存する）
    @Column(name = "recipient", nullable = false, length = 20)
    private String recipient;

    @Column(name = "phone_number", nullable = false, length = 20)
    private String phoneNumber;

    @Column(name = "postal_code", nullable = false, length = 10)
    private String postalCode;

    @Column(name = "prefecture", nullable = false, length = 10)
    private String prefecture;

    @Column(name = "city", nullable = false, length = 20)
    private String city;

    @Column(name = "address_line1", nullable = false, length = 50)
    private String addressLine1;

    @Column(name = "address_line2", length = 50)
    private String addressLine2;

    // ▼ 合計金額
    @Column(name = "total_amount", nullable = false)
    private Integer totalAmount;

    // ▼ 注文日時
    @Column(name = "ordered_at", nullable = false)
    private LocalDateTime orderedAt;

    // ▼ 注文明細（1注文に複数の商品）
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items = new ArrayList<>();

    @PrePersist
    public void onCreate() {
        this.orderedAt = LocalDateTime.now();
    }
}
