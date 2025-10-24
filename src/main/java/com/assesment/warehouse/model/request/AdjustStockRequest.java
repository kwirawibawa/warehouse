package com.assesment.warehouse.model.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AdjustStockRequest {
    @NotNull
    private Long id;

    @NotNull
    private Long adjustStock;
}
