package com.assesment.warehouse.model.response;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class VariantResponse {
    private Long id;
    private String sku;
    private String name;
    private BigDecimal price;
    private Long stock;
    private Long itemId;
}

