package com.example.customer.dto;

import com.example.customer.contants.Template;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationDto {
    private Template template;
    private String title;
    private String receiver;
    private Map<String, Object> value;
}
