package com.project.foodapp.service;

import com.project.foodapp.entity.FoodItem;
import com.project.foodapp.repository.FoodItemRepository;
import com.project.foodapp.repository.RestaurantRepository;
import com.project.foodapp.repository.RestaurantRepository;
import com.project.foodapp.entity.Restaurant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FoodItemService {

    @Autowired
    private FoodItemRepository repository;

    // ✅ Create item
    @Autowired
    private RestaurantRepository restaurantRepository;

    public FoodItem create(FoodItem item) {

        if (item.getRestaurant() == null || item.getRestaurant().getId() == null) {
            throw new RuntimeException("Restaurant is required");
        }

        Long restaurantId = item.getRestaurant().getId();

        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RuntimeException("Restaurant not found with id: " + restaurantId));

        item.setRestaurant(restaurant);

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