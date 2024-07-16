package com.example.notification.dto;

import com.example.notification.contants.Template;
import com.example.notification.contants.Type;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationDto {
    private Type type;
    private Template template;
    private String title;
    private String receiver;
    private Map<String, Object> value;
    private String message;

    public boolean isValid() {
        return switch (this.type) {
            case MAIL -> Objects.isNull(value) || Objects.isNull(template) || Objects.isNull(title)
                    || Objects.isNull(receiver);
            case SMS -> Objects.isNull(message) || Objects.isNull(title) || Objects.isNull(receiver);
        };
    }
}