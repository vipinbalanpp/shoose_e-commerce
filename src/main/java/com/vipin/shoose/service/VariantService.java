package com.vipin.shoose.service;

import com.vipin.shoose.model.Variant;

import java.util.List;

public interface VariantService {
    List<Variant> getVaraints(Long id);

    List<String> getColorsByProductId(Long productId);

    void changeQuantity(Long variantId, Long quantity,Long productId);

    void addNewVarinats(Long productId, List<String> variantColors, List<Long> variantSizes, List<Integer> variantQuantities);

    List<Long> getAllSizesForColor(Long productId, String selectedColor);
}
