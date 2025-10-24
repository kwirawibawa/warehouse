package com.assesment.warehouse.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.assesment.warehouse.model.request.VariantRequest;
import com.assesment.warehouse.model.response.BaseResponse;
import com.assesment.warehouse.service.VariantService;

import java.util.Map;

@RestController
@RequestMapping("/variant")
@RequiredArgsConstructor
public class VariantController {

    private final VariantService variantService;

    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> createVariant(@RequestBody @Valid VariantRequest request) {
        var created = variantService.createVariant(request);
        return ResponseEntity.ok(BaseResponse.success("Variant created successfully", created).toMap());
    }

    @GetMapping("getVariants")
    public ResponseEntity<Map<String, Object>> getAllVariants() {
        var variants = variantService.getAllVariants();
        return ResponseEntity.ok(BaseResponse.success("Variants retrieved successfully", variants).toMap());
    }

    @GetMapping("{id}")
    public ResponseEntity<Map<String, Object>> getVariantById(@PathVariable Long id) {
        var variant = variantService.getVariantById(id);
        return ResponseEntity.ok(BaseResponse.success("Variant found", variant).toMap());
    }

    @GetMapping("/sku/{sku}")
    public ResponseEntity<Map<String, Object>> getVariantBySku(@PathVariable String sku) {
        var variant = variantService.getVariantBySku(sku);
        return ResponseEntity.ok(BaseResponse.success("Variant found", variant).toMap());
    }

    @PutMapping("/update")
    public ResponseEntity<Map<String, Object>> updateVariant(@RequestBody @Valid VariantRequest request) {
        var updated = variantService.updateVariant(request.getId(), request);
        return ResponseEntity.ok(BaseResponse.success("Variant updated successfully", updated).toMap());
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Map<String, Object>> deleteVariant(@PathVariable Long id) {
        variantService.deleteVariant(id);
        return ResponseEntity.ok(BaseResponse.success("Variant deleted successfully", null).toMap());
    }
}
