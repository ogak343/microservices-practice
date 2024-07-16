package com.example.notification.dto;

import com.example.notification.contants.Template;
import com.example.notification.contants.Type;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationDto {
    private Type type;
    private Template template;
    private boolean withAttachment;
    private String title;
    private String receiver;
    private Map<String, Object> message;
}
