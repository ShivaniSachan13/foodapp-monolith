package com.project.foodapp.service;

import com.project.foodapp.entity.FoodItem;
import com.project.foodapp.repository.FoodItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FoodItemService {

    @Autowired
    private FoodItemRepository repository;

    // ✅ Create item
    public FoodItem create(FoodItem item) {
        return repository.save(item);
    }

    // ✅ Get all items
    public List<FoodItem> getAll() {
        return repository.findAll();
    }

    // ✅ Get item by ID
    public FoodItem getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Food item not found"));
    }

    // ✅ Update item
    public FoodItem update(Long id, FoodItem item) {
        FoodItem existing = getById(id);

        existing.setName(item.getName());
        existing.setDescription(item.getDescription());
        existing.setPrice(item.getPrice());
        existing.setCategory(item.getCategory());
        existing.setAvailable(item.getAvailable());

        return repository.save(existing);
    }

    // ✅ Delete item
    public void delete(Long id) {
        FoodItem existing = getById(id);
        repository.delete(existing);
    }
}