package com.assesment.warehouse.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AdjustStockBySkuRequest {
    @NotBlank
    private String sku;

    @NotNull
    private Long adjustStock;
}
