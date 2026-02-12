package katachi.example.toretatebox.domain.model;

import lombok.Data;

/**
 * カート用の商品情報クラス
 */
@Data
public class CartItem {

    private Integer productId; 
    
    private String imageUrl;
    
    private String name;
    
    private int price;
    
    private int quantity;
    /**
     * 小計（価格 × 数量）
     */
    public int getSubtotal() {
        return price * quantity;
    }
}
