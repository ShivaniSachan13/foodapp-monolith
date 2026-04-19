package com.project.foodapp.controller;

import com.project.foodapp.entity.FoodItem;
import com.project.foodapp.response.ApiResponse;
import com.project.foodapp.service.FoodItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/items")
public class FoodItemController {

    @Autowired
    private FoodItemService service;

    // ✅ Create item
    @PostMapping
    public ApiResponse<FoodItem> create(@RequestBody FoodItem item) {
        return new ApiResponse<>(
                true,
                "Item created successfully",
                service.create(item)
        );
    }

    // ✅ Get all items
    @GetMapping
    public ApiResponse<List<FoodItem>> getAll() {
        return new ApiResponse<>(
                true,
                "Items fetched successfully",
                service.getAll()
        );
    }

    // ✅ Get item by ID
    @GetMapping("/{id}")
    public ApiResponse<FoodItem> getById(@PathVariable Long id) {
        return new ApiResponse<>(
                true,
                "Item fetched successfully",
                service.getById(id)
        );
    }

    // ✅ Update item
    @PutMapping("/{id}")
    public ApiResponse<FoodItem> update(
            @PathVariable Long id,
            @RequestBody FoodItem item) {

        return new ApiResponse<>(
                true,
                "Item updated successfully",
                service.update(id, item)
        );
    }

    // ✅ Delete item
    @DeleteMapping("/{id}")
    public ApiResponse<String> delete(@PathVariable Long id) {
        service.delete(id);
        return new ApiResponse<>(
                true,
                "Item deleted successfully",
                null
        );
    }
}