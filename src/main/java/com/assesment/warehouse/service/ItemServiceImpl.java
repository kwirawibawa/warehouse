package com.assesment.warehouse.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.assesment.warehouse.model.entity.Item;
import com.assesment.warehouse.model.entity.Variant;
import com.assesment.warehouse.model.request.ItemRequest;
import com.assesment.warehouse.model.response.ItemResponse;
import com.assesment.warehouse.model.response.VariantResponse;
import com.assesment.warehouse.repository.ItemRepository;
import com.assesment.warehouse.repository.VariantRepository;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final VariantRepository variantRepository;

    @Override
    public ItemResponse createItem(ItemRequest request) {
        Item item = new Item();
        item.setName(request.getName());
        item.setDescription(request.getDescription());

        List<Variant> variants = request.getVariants().stream()
                .map(vr -> {
                    Variant variant = new Variant();
                    variant.setSku(vr.getSku());
                    variant.setName(vr.getName());
                    variant.setPrice(java.math.BigDecimal.valueOf(vr.getPrice()));
                    variant.setStock(vr.getStock().longValue());
                    variant.setItem(item);
                    return variant;
                }).collect(Collectors.toList());

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
        return itemRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ItemResponse updateItem(Long id, ItemRequest request) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Item not found with ID: " + id));

        item.setName(request.getName());
        item.setDescription(request.getDescription());

        variantRepository.deleteAll(item.getVariants());
        List<Variant> variants = request.getVariants().stream()
                .map(vr -> {
                    Variant variant = new Variant();
                    variant.setSku(vr.getSku());
                    variant.setName(vr.getName());
                    variant.setPrice(java.math.BigDecimal.valueOf(vr.getPrice()));
                    variant.setStock(vr.getStock().longValue());
                    variant.setItem(item);
                    return variant;
                }).collect(Collectors.toList());

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
            List<VariantResponse> variantResponses = item.getVariants().stream().map(v -> {
                VariantResponse vr = new VariantResponse();
                vr.setId(v.getId());
                vr.setSku(v.getSku());
                vr.setName(v.getName());
                vr.setPrice(v.getPrice());
                vr.setStock(v.getStock());
                vr.setItemId(v.getItem().getId());
                return vr;
            }).collect(Collectors.toList());
            response.setVariants(variantResponses);
        }
        return response;
    }
}