package com.vipin.shoose.service;

import com.vipin.shoose.model.Variant;

import java.util.List;

public interface VariantService {
    List<Variant> getVaraints(Long id);

    List<String> getColorsByProductId(Long productId);
}
