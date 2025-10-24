package com.assesment.warehouse.service;

import com.assesment.warehouse.model.request.VariantRequest;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.assesment.warehouse.model.entity.Item;
import com.assesment.warehouse.model.entity.Variant;
import com.assesment.warehouse.model.request.ItemRequest;
import com.assesment.warehouse.model.response.ItemResponse;
import com.assesment.warehouse.model.response.VariantResponse;
import com.assesment.warehouse.repository.ItemRepository;
import com.assesment.warehouse.repository.VariantRepository;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final VariantRepository variantRepository;

    @Override
    @Transactional
    public ItemResponse createItem(ItemRequest request) {
        if (itemRepository.existsByNameIgnoreCase(request.getName())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Item with name '" + request.getName() + "' already exists");
        }

        Item item = new Item();
        item.setName(request.getName());
        item.setDescription(request.getDescription());

        List<Variant> variants = new ArrayList<>();

        if (request.getVariants() != null && !request.getVariants().isEmpty()) {
            for (VariantRequest variantRequest : request.getVariants()) {
                if (variantRepository.existsBySkuIgnoreCase(variantRequest.getSku())) {
                    throw new ResponseStatusException(HttpStatus.CONFLICT,
                            "Variant with SKU '" + variantRequest.getSku() + "' already exists");
                }

                if (variantRequest.getPrice() == null || variantRequest.getPrice() <= 0) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                            "Price must be greater than 0 for variant '" + variantRequest.getSku() + "'");
                }

                if (variantRequest.getStock() == null || variantRequest.getStock() < 0) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                            "Stock cannot be negative for variant '" + variantRequest.getSku() + "'");
                }

                Variant variant = new Variant();
                variant.setSku(variantRequest.getSku());
                variant.setName(variantRequest.getName());
                variant.setPrice(BigDecimal.valueOf(variantRequest.getPrice()));
                variant.setStock(variantRequest.getStock().longValue());
                variant.setItem(item);
                variants.add(variant);
            }
        }

        item.setVariants(variants);
        Item saved = itemRepository.save(item);
        return mapToResponse(saved);
    }

    @Override
    public ItemResponse getItemById(Long id) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Item not found with ID: " + id));
        return mapToResponse(item);
    }

    @Override
    public List<ItemResponse> getAllItems() {
        List<ItemResponse> list = new ArrayList<>();
        for (Item item : itemRepository.findAll()) {
            ItemResponse itemResponse = mapToResponse(item);
            list.add(itemResponse);
        }
        return list;
    }

    @Override
    public ItemResponse updateItem(Long id, ItemRequest request) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Item not found with ID: " + id));

        item.setName(request.getName());
        item.setDescription(request.getDescription());

        variantRepository.deleteAll(item.getVariants());
        List<Variant> variants = new ArrayList<>();
        for (VariantRequest variantRequest : request.getVariants()) {
            Variant variant = new Variant();
            variant.setSku(variantRequest.getSku());
            variant.setName(variantRequest.getName());
            variant.setPrice(BigDecimal.valueOf(variantRequest.getPrice()));
            variant.setStock(variantRequest.getStock().longValue());
            variant.setItem(item);
            variants.add(variant);
        }

        item.setVariants(variants);
        Item updated = itemRepository.save(item);
        return mapToResponse(updated);
    }

    @Override
    public void deleteItem(Long id) {
        if (!itemRepository.existsById(id)) {
            throw new EntityNotFoundException("Item not found with ID: " + id);
        }
        itemRepository.deleteById(id);
    }

    private ItemResponse mapToResponse(Item item) {
        ItemResponse response = new ItemResponse();
        response.setId(item.getId());
        response.setName(item.getName());
        response.setDescription(item.getDescription());

        if (item.getVariants() != null) {
            List<VariantResponse> variantResponses = new ArrayList<>();
            for (Variant variant : item.getVariants()) {
                VariantResponse vr = new VariantResponse();
                vr.setId(variant.getId());
                vr.setSku(variant.getSku());
                vr.setName(variant.getName());
                vr.setPrice(variant.getPrice());
                vr.setStock(variant.getStock());
                vr.setItemId(variant.getItem().getId());
                variantResponses.add(vr);
            }
            response.setVariants(variantResponses);
        }
        return response;
    }
}