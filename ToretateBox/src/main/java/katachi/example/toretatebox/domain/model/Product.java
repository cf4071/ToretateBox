package katachi.example.toretatebox.domain.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "products")
@Data
public class Product {

    // 1. 食材ID
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    // 2. 食材名
    @Column(name = "name", nullable = false, length = 50)
    private String name;

    // 3. 食材説明
    @Column(name = "description", length = 255)
    private String description;

    // 4. 値段
    @Column(name = "price", nullable = false)
    private Integer price;

    // 5. カテゴリID（外部キー）
    @Column(name = "category_id", nullable = false)
    private Integer categoryId;

    // 6. 旬の季節
    @Column(name = "season", length = 20)
    private String season;

    // 7. 画像URL
    @Column(name = "image_url", length = 255)
    private String imageUrl;

    // 8. 表示フラグ（TRUE / FALSE）
    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    // 9. 登録日時
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // 10. 更新日時
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updateAt;

    // ==============================
    // ▼ Category リレーション
    // ==============================
    @ManyToOne
    @JoinColumn(name = "category_id", insertable = false, updatable = false)
    private Category category;

    // ==============================
    // 新規保存時の自動セット
    // ==============================
    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updateAt = LocalDateTime.now();
    }

    // ==============================
    // 更新時の自動セット
    // ==============================
    @PreUpdate
    public void onUpdate() {
        this.updateAt = LocalDateTime.now();
    }
}
