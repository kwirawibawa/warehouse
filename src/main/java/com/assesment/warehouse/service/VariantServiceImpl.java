package com.assesment.warehouse.service;

import com.assesment.warehouse.model.request.SellBySkuRequest;
import com.assesment.warehouse.model.response.SellResultResponse;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.assesment.warehouse.exception.NotEnoughStockException;
import com.assesment.warehouse.model.entity.Item;
import com.assesment.warehouse.model.entity.Variant;
import com.assesment.warehouse.model.request.SellMultipleRequest;
import com.assesment.warehouse.model.request.VariantRequest;
import com.assesment.warehouse.model.request.SellRequest;
import com.assesment.warehouse.model.response.VariantResponse;
import com.assesment.warehouse.repository.ItemRepository;
import com.assesment.warehouse.repository.VariantRepository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class VariantServiceImpl implements VariantService {

    private final VariantRepository variantRepository;
    private final ItemRepository itemRepository;

    @Transactional
    @Override
    public VariantResponse createVariant(Long itemId, VariantRequest request) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new EntityNotFoundException("Item not found with ID: " + itemId));

        Variant variant = Variant.builder()
                .sku(request.getSku())
                .name(request.getName())
                .price(BigDecimal.valueOf(request.getPrice()))
                .stock(request.getStock().longValue())
                .item(item)
                .build();

        Variant saved = variantRepository.save(variant);
        return toResponse(saved);
    }

    @Override
    public List<VariantResponse> getAllVariants() {
        return variantRepository.findAll().stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    public VariantResponse getVariantById(Long id) {
        Variant variant = variantRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Variant not found with ID: " + id));
        return toResponse(variant);
    }

    @Transactional
    @Override
    public VariantResponse updateVariant(Long id, VariantRequest request) {
        Variant variant = variantRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Variant not found with ID: " + id));

        variant.setSku(request.getSku());
        variant.setName(request.getName());
        variant.setPrice(BigDecimal.valueOf(request.getPrice()));
        variant.setStock(request.getStock().longValue());

        Variant updated = variantRepository.save(variant);
        return toResponse(updated);
    }

    @Transactional
    @Override
    public void deleteVariant(Long id) {
        Variant variant = variantRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Variant not found with ID: " + id));
        variantRepository.delete(variant);
    }

    @Transactional
    @Override
    public SellResultResponse reduceStock(SellRequest request) {
        Long id = request.getId();
        int qty = request.getQuantity();

        Variant variant = variantRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Variant not found with ID : " + id));

        log.info("Attempting to reduce stock for id={} qty={} currentStock={}", id, qty, variant.getStock());

        if (variant.getStock() < qty) {
            throw new NotEnoughStockException("Not enough stock for SKU: " + id);
        }

        variant.setStock(variant.getStock() - qty);
        Variant saved = variantRepository.save(variant);

        return new SellResultResponse(saved.getSku(), (long) qty, saved.getStock());
    }

    @Transactional
    @Override
    public SellResultResponse reduceStockBySku(SellBySkuRequest request) {
        String sku = request.getSku();
        int qty = request.getQuantity();

        Variant variant = variantRepository.findBySku(sku)
                .orElseThrow(() -> new EntityNotFoundException("Variant not found with SKU: " + sku));

        log.info("Attempting to reduce stock for sku={} qty={} currentStock={}", sku, qty, variant.getStock());

        if (variant.getStock() < qty) {
            throw new NotEnoughStockException("Not enough stock for SKU: " + sku);
        }

        variant.setStock(variant.getStock() - qty);
        Variant saved = variantRepository.save(variant);

        return new SellResultResponse(saved.getSku(), (long) qty, saved.getStock());
    }

    @Override
    public VariantResponse getStockInfoById(Long id) {
        Variant variant = variantRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Variant not found: " + id));
        return toResponse(variant);
    }

    @Override
    public VariantResponse getStockInfoBySku(String sku) {
        Variant variant = variantRepository.findBySku(sku)
                .orElseThrow(() -> new EntityNotFoundException("Variant not found: " + sku));
        return toResponse(variant);
    }

    @Transactional
    @Override
    public VariantResponse updateStockById(Long id, Long adjustStock) {
        Variant variant = variantRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Variant not found: " + id));
        long newStock = variant.getStock() + adjustStock;
        if (newStock < 0) {
            throw new NotEnoughStockException("Operation would make stock negative for SKU: " + id);
        }
        variant.setStock(newStock);
        Variant saved = variantRepository.save(variant);
        return toResponse(saved);
    }

    @Transactional
    @Override
    public VariantResponse updateStockBySku(String sku, Long adjustStock) {
        Variant variant = variantRepository.findBySku(sku)
                .orElseThrow(() -> new EntityNotFoundException("Variant not found: " + sku));
        long newStock = variant.getStock() + adjustStock;
        if (newStock < 0) {
            throw new NotEnoughStockException("Operation would make stock negative for SKU: " + sku);
        }
        variant.setStock(newStock);
        Variant saved = variantRepository.save(variant);
        return toResponse(saved);
    }

    @Override
    public VariantResponse getVariantBySku(String sku) {
        Variant v = variantRepository.findBySku(sku)
                .orElseThrow(() -> new EntityNotFoundException("Variant not found: " + sku));
        return toResponse(v);
    }

    @Transactional
    @Override
    public List<SellResultResponse> reduceStockBulk(SellMultipleRequest request) {
        var items = request.getVariants();
        Map<String, Variant> variantMap = new HashMap<>();
        List<SellResultResponse> results = new ArrayList<>();

        for (var item : items) {
            Variant variant = variantRepository.findBySku(item.getSku())
                    .orElseThrow(() -> new EntityNotFoundException("Variant not found: " + item.getSku()));
            if (variant.getStock() < item.getQuantity()) {
                throw new NotEnoughStockException("Not enough stock for SKU: " + item.getSku());
            }
            variantMap.put(item.getSku(), variant);
        }

        for (var item : items) {
            Variant variant = variantMap.get(item.getSku());
            long newStock = variant.getStock() - item.getQuantity();
            variant.setStock(newStock);
            variantRepository.save(variant);

            results.add(new SellResultResponse(
                    variant.getSku(),
                    item.getQuantity(),
                    newStock
            ));
            log.info("Reduced {} units of SKU {}", item.getQuantity(), item.getSku());
        }

        return results;
    }

    private VariantResponse toResponse(Variant variant) {
        VariantResponse response = new VariantResponse();
        response.setId(variant.getId());
        response.setSku(variant.getSku());
        response.setName(variant.getName());
        response.setPrice(variant.getPrice());
        response.setStock(variant.getStock());
        response.setItemId(variant.getItem() != null ? variant.getItem().getId() : null);
        return response;
    }
}
