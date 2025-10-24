package com.assesment.warehouse.controller;

import com.assesment.warehouse.model.request.*;
import com.assesment.warehouse.model.response.BaseResponse;
import com.assesment.warehouse.service.ItemService;
import com.assesment.warehouse.service.VariantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.util.Map;

@RestController
@RequestMapping("/warehouse")
@RequiredArgsConstructor
public class WarehouseController {

    private final ItemService itemService;
    private final VariantService variantService;

    @PostMapping("/sell")
    public ResponseEntity<Map<String, Object>> sellItem(@RequestBody @Valid SellRequest request) {
        var result = variantService.reduceStock(request);
        return ResponseEntity.ok(BaseResponse.success("Sold successfully", result).toMap());
    }

    @PostMapping("/sku/sell")
    public ResponseEntity<Map<String, Object>> sellItemBySku(@RequestBody @Valid SellBySkuRequest request) {
        var result = variantService.reduceStockBySku(request);
        return ResponseEntity.ok(BaseResponse.success("Sold successfully", result).toMap());
    }

    @GetMapping("/stock/{id}")
    public ResponseEntity<Map<String, Object>> getStockById(@PathVariable Long id) {
        var resp = variantService.getStockInfoById(id);
        return ResponseEntity.ok(BaseResponse.success(resp).toMap());
    }

    @GetMapping("/stock/sku/{sku}")
    public ResponseEntity<Map<String, Object>> getStockBySku(@PathVariable String sku) {
        var resp = variantService.getStockInfoBySku(sku);
        return ResponseEntity.ok(BaseResponse.success(resp).toMap());
    }

    @PatchMapping("/stock/adjust")
    public ResponseEntity<Map<String, Object>> adjustStockById(@RequestBody @Valid AdjustStockRequest request) {
        var resp = variantService.updateStockById(request.getId(), request.getAdjustStock());
        return ResponseEntity.ok(BaseResponse.success("Stock adjusted", resp).toMap());
    }

    @PatchMapping("/stock/sku/adjust")
    public ResponseEntity<Map<String, Object>> adjustStockBySku(@RequestBody @Valid AdjustStockBySkuRequest request) {
        var resp = variantService.updateStockBySku(request.getSku(), request.getAdjustStock());
        return ResponseEntity.ok(BaseResponse.success("Stock adjusted", resp).toMap());
    }

    @PostMapping("/sell/bulk")
    public ResponseEntity<Map<String, Object>> sellMultiple(@RequestBody @Valid SellMultipleRequest request) {
        var result = variantService.reduceStockBulk(request);
        return ResponseEntity.ok(BaseResponse.success("Bulk sell completed successfully", result).toMap());
    }

    @GetMapping("/stock/getAll")
    public ResponseEntity<Map<String, Object>> getAllStock() {
        var allStock = variantService.getAllStock();
        return ResponseEntity.ok(
                BaseResponse.success("All variant stock retrieved successfully", allStock).toMap()
        );
    }
}
