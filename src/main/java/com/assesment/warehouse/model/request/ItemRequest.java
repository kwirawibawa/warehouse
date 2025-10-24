package com.assesment.warehouse.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class ItemRequest {
    private Long id;

    @NotBlank
    private String name;

    @NotBlank
    private String description;

    private List<VariantRequest> variants;
}
