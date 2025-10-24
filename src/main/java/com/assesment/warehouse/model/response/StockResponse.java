package com.assesment.warehouse.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StockResponse {
    private Long variantId;
    private String sku;
    private String name;
    private Long stock;
    private Long itemId;
    private String itemName;
}

