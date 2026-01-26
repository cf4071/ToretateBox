package katachi.example.toretatebox.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrderItemView {
    private String productName;
    private int quantity;
    private int unitPrice;

    public int getSubtotal() {
        return unitPrice * quantity;
    }
}
