package com.vipin.shoose.service;

import com.vipin.shoose.dto.ColorDto;
import com.vipin.shoose.model.Color;
import com.vipin.shoose.repository.ColorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ColorServiceImpl implements ColorService {
    @Autowired
    ColorRepository colorRepository;
    @Override
    public void addColour(ColorDto colorDto) {
        Color color=new Color();
        color.setColorName(colorDto.getColorName());
        colorRepository.save(color);
    }
}
