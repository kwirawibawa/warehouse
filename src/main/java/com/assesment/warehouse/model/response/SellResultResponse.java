package com.assesment.warehouse.model.response;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SellResultResponse {
    private String sku;
    private Long soldQuantity;
    private Long remainingStock;

    public SellResultResponse(String sku, @Min(1) Integer quantity, long newStock) {
        this.sku = sku;
        this.soldQuantity = quantity.longValue();
        this.remainingStock = newStock;
    }
}
