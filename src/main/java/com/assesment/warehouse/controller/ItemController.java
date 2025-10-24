package com.assesment.warehouse.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.assesment.warehouse.model.request.ItemRequest;
import com.assesment.warehouse.model.response.BaseResponse;
import com.assesment.warehouse.service.ItemService;

@RestController
@RequestMapping("/item")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @PostMapping("/create")
    public ResponseEntity<?> createItem(@RequestBody @Valid ItemRequest request) {
        var created = itemService.createItem(request);
        return ResponseEntity.ok(BaseResponse.success("Item created successfully", created).toMap());
    }

    @GetMapping("/getItems")
    public ResponseEntity<?> getAllItems() {
        var items = itemService.getAllItems();
        return ResponseEntity.ok(BaseResponse.success("Items retrieved successfully", items).toMap());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getItemById(@PathVariable Long id) {
        var item = itemService.getItemById(id);
        return ResponseEntity.ok(BaseResponse.success("Item found", item).toMap());
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateItem(@RequestBody @Valid ItemRequest request) {
        var updated = itemService.updateItem(request.getId(), request);
        return ResponseEntity.ok(BaseResponse.success("Item updated successfully", updated).toMap());
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteItem(@PathVariable Long id) {
        itemService.deleteItem(id);
        return ResponseEntity.ok(BaseResponse.success("Item deleted successfully", null).toMap());
    }
}
