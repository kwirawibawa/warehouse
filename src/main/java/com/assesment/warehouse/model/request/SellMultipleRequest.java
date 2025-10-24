package com.assesment.warehouse.model.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class SellMultipleRequest {

    @NotEmpty(message = "Variants list cannot be empty")
    private List<ItemSell> variants;

    @Data
    public static class ItemSell {
        @NotBlank
        private String sku;

        @Min(1)
        private Integer quantity;
    }
}
