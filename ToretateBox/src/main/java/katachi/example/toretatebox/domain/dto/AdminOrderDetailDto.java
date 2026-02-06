package katachi.example.toretatebox.domain.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;

@Data
public class AdminOrderDetailDto {
    private Long orderId;
    private LocalDateTime createdAt;

    private String customerName;
    private String phone;
    private String email;

    private String postalCode;
    private String prefecture;
    private String city;
    private String addressLine;
    private String building;

    private List<AdminOrderItemDto> items;

    private Integer totalAmount;
}
