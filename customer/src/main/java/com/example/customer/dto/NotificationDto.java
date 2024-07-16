package com.example.customer.dto;

import com.example.customer.contants.Template;
import com.example.customer.contants.Type;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationDto {
    private Type type;
    private boolean withAttachment;
    private Template template;
    private String receiver;
    private String message;
}
