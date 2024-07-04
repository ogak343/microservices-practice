package com.example.customer.dto;

import com.example.customer.contants.Type;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationDto {
    private Type type;
    private String receiver;
    private String message;
}
