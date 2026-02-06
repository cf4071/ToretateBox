package katachi.example.toretatebox.domain.dto;

import lombok.Data;

@Data
public class AdminOrderItemDto {
    private String productName;
    private Integer quantity;
    private Integer unitPrice;
    private Integer subtotal;
}
