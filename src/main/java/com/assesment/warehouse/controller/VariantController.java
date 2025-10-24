package com.assesment.warehouse.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.assesment.warehouse.model.request.VariantRequest;
import com.assesment.warehouse.model.response.BaseResponse;
import com.assesment.warehouse.service.VariantService;

@RestController
@RequestMapping("/variant")
@RequiredArgsConstructor
public class VariantController {

    private final VariantService variantService;

    @PostMapping("/create")
    public ResponseEntity<?> createVariant(@RequestBody @Valid VariantRequest request) {
        var created = variantService.createVariant(request.getItemId(), request);
        return ResponseEntity.ok(BaseResponse.success("Variant created successfully", created).toMap());
    }

    @GetMapping("getVariants")
    public ResponseEntity<?> getAllVariants() {
        var variants = variantService.getAllVariants();
        return ResponseEntity.ok(BaseResponse.success("Variants retrieved successfully", variants).toMap());
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getVariantById(@PathVariable Long id) {
        var variant = variantService.getVariantById(id);
        return ResponseEntity.ok(BaseResponse.success("Variant found", variant).toMap());
    }

    @GetMapping("/sku/{sku}")
    public ResponseEntity<?> getVariantById(@PathVariable String sku) {
        var variant = variantService.getVariantBySku(sku);
        return ResponseEntity.ok(BaseResponse.success("Variant found", variant).toMap());
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateVariant(@RequestBody @Valid VariantRequest request) {
        var updated = variantService.updateVariant(request.getId(), request);
        return ResponseEntity.ok(BaseResponse.success("Variant updated successfully", updated).toMap());
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteVariant(@PathVariable Long id) {
        variantService.deleteVariant(id);
        return ResponseEntity.ok(BaseResponse.success("Variant deleted successfully", null).toMap());
    }
}
