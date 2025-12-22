package katachi.example.toretatebox.domain.model;

import lombok.Data;

/**
 * カート用の商品情報クラス
 */
@Data
public class CartItem {

    /** 商品ID */
    private Integer productId;
    
    private String imageUrl;

    /** 商品名 */
    private String name;

    /** 価格（1個あたり） */
    private int price;

    /** 数量 */
    private int quantity;

    /**
     * 小計（価格 × 数量）
     */
    public int getSubtotal() {
        return price * quantity;
    }
}
