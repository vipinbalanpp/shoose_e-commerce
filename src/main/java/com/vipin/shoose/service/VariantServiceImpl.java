package com.vipin.shoose.service;

import com.vipin.shoose.model.Variant;
import com.vipin.shoose.repository.VariantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VariantServiceImpl implements  VariantService{
    @Autowired
    VariantRepository variantRepository;

    @Override
    public List<Variant> getVaraints(Long id) {
        return variantRepository.findByProductId(id);
    }

    @Override
    public List<String> getColorsByProductId(Long productId) {

        return variantRepository.findColorsByProductId(productId);
    }
}
