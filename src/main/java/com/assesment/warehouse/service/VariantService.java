package com.assesment.warehouse.service;

import com.assesment.warehouse.model.request.SellBySkuRequest;
import com.assesment.warehouse.model.request.SellMultipleRequest;
import com.assesment.warehouse.model.request.SellRequest;
import com.assesment.warehouse.model.request.VariantRequest;
import com.assesment.warehouse.model.response.SellResultResponse;
import com.assesment.warehouse.model.response.StockResponse;
import com.assesment.warehouse.model.response.VariantResponse;
import java.util.List;

public interface VariantService {
    VariantResponse createVariant(VariantRequest request);
    List<VariantResponse> getAllVariants();
    VariantResponse getVariantById(Long id);
    VariantResponse updateVariant(Long id, VariantRequest request);
    void deleteVariant(Long id);
    SellResultResponse reduceStock(SellRequest request);
    SellResultResponse reduceStockBySku(SellBySkuRequest request);
    VariantResponse getStockInfoById(Long id);
    VariantResponse getStockInfoBySku(String sku);
    VariantResponse updateStockById(Long ind, Long adjustStock);
    VariantResponse updateStockBySku(String sku, Long adjustStock);
    VariantResponse getVariantBySku(String sku);
    List<SellResultResponse> reduceStockBulk(SellMultipleRequest request);
    List<StockResponse> getAllStock();
}