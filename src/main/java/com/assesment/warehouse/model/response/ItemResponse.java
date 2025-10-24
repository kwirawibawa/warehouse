package com.assesment.warehouse.model.response;

import lombok.Data;

import java.util.List;

@Data
public class ItemResponse {
    private Long id;
    private String name;
    private String description;
    private List<VariantResponse> variants;
}
