package com.vipin.shoose.service;

import com.vipin.shoose.model.Variant;

import java.util.List;
import java.util.Map;

public interface VariantService {
    List<Variant> getVaraints(Long id);

    List<String> getColorsByProductId(Long productId);

    void changeQuantity(Long variantId, Long quantity,Long productId);

    void addNewVarinats(Long productId, List<String> variantColors, List<Long> variantSizes, List<Integer> variantQuantities);

    List<Long> getAllSizesForColor(Long productId, String selectedColor);

    Variant getVariantByProductColorAndSize(Long productId, String color, Long size);

    Long getVariantQuantity(Long productId, String color, Long size);

    void manageStockForOrder(Map<Variant, Long> orderProducts);
    void updateStockonCancellingOrder(Long orderId);
}
