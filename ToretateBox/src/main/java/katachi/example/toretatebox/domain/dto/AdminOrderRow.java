package katachi.example.toretatebox.domain.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AdminOrderRow {
    private Integer id;
    private String name;
    private String email;
    private LocalDateTime createdAt;
    private int totalAmount;
}
