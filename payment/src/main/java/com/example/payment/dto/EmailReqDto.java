package com.example.payment.dto;

import com.example.payment.constants.Template;
import lombok.Builder;

import java.util.Map;

@Builder
public record EmailReqDto(
        Template template,
        String title,
        String receiver,
        Map<String, Object> value
) {

}
