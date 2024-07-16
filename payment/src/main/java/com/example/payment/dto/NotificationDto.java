package com.example.payment.dto;

import com.example.payment.constants.Template;
import com.example.payment.constants.Type;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NotificationDto {
    private Type type;
    private boolean withAttachment;
    private Template template;
    private String receiver;
    private String message;
}
