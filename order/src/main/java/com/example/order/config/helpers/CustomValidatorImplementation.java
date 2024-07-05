package com.example.order.config.helpers;

import com.example.order.annoration.Unrepeated;
import com.example.order.dto.req.ProductDetailsDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class CustomValidatorImplementation
        implements ConstraintValidator<Unrepeated, Collection<ProductDetailsDto>> {

    @Override
    public boolean isValid(Collection<ProductDetailsDto> dto, ConstraintValidatorContext validator) {

        Map<Long, Boolean> map = new HashMap<>();
        AtomicBoolean result = new AtomicBoolean(true);
        dto.forEach(x -> {
            if (map.get(x.getDetailId()) != null) {
                result.set(false);
            } else {
                map.put(x.getDetailId(), true);
            }
        });

        return result.get();
    }
}
