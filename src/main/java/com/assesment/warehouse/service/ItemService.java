package com.assesment.warehouse.service;

import com.assesment.warehouse.model.request.ItemRequest;
import com.assesment.warehouse.model.response.ItemResponse;
import java.util.List;

public interface ItemService {
    ItemResponse createItem(ItemRequest request);
    ItemResponse getItemById(Long id);
    List<ItemResponse> getAllItems();
    ItemResponse updateItem(Long id, ItemRequest request);
    void deleteItem(Long id);
}
