package katachi.example.toretatebox.domain.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "users")
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private int id;
	
	@Column(name = "name", nullable = false, length = 20)
	private String name;
	
	@Column(name = "name_kana", nullable = false, length = 20)
	private String name_kana;
	
	@Column(name = "phone_number", nullable = false, length = 20)
	private String phone_number;
	
	@Column(name = "email", nullable = false, length = 254)
	private String email;
	
	@Column(name = "password", nullable = false, length = 256)
	private String password;
	
	@Column(name = "is_admin", nullable = false)
	private boolean is_admin;
	
	@Column(name = "is_deleted", nullable = false)
	private boolean is_deleted;
	
	@Column(name = "created_at", nullable = false)
	private LocalDateTime created_at;
	
	@Column(name = "updated_at", nullable = false)
	private LocalDateTime updated_at;
	
	@PrePersist
    public void onCreate() {
        this.created_at = LocalDateTime.now();
        this.updated_at = LocalDateTime.now();
    }

    @PreUpdate
    public void onUpdate() {
        this.updated_at = LocalDateTime.now();
    }
}
