package com.assesment.warehouse.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class VariantRequest {
    private Long id;

    @NotNull
    private Long itemId;

    @NotBlank
    private String sku;

    @NotBlank
    private String name;

    @NotNull
    private Double price;

    @NotNull
    private Integer stock;
}
