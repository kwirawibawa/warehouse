package com.assesment.warehouse.model.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SellBySkuRequest {
    @NotBlank
    private String sku;

    @Min(1)
    private Integer quantity;
}
