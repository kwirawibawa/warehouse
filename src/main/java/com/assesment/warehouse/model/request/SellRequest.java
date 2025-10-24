package com.assesment.warehouse.model.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SellRequest {
    @NotNull
    private Long id;

    @Min(1)
    private Integer quantity;
}
