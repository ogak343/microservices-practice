package com.example.order.config.helpers;

import com.example.order.annoration.Unrepeated;
import com.example.order.dto.req.ProductDetailsDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Set;
import java.util.HashSet;
import java.util.concurrent.atomic.AtomicBoolean;

@Component
public class CustomValidatorImplementation
        implements ConstraintValidator<Unrepeated, Collection<ProductDetailsDto>> {

    @Override
    public boolean isValid(Collection<ProductDetailsDto> dto, ConstraintValidatorContext validator) {

        Set<Long> set = new HashSet<>();
        AtomicBoolean result = new AtomicBoolean(true);
        dto.forEach(x -> {
            if (set.contains(x.getDetailId())) {
                result.set(false);
            } else {
                set.add(x.getDetailId());
            }
        });

        return result.get();
    }
}
