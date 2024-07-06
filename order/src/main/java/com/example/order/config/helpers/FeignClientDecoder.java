package com.example.order.config.helpers;

import com.example.order.config.exception.ClientException;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Configuration;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

@Configuration
public class FeignClientDecoder implements ErrorDecoder {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Exception decode(String s, Response response) {
        String message;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(response.body().asInputStream(), StandardCharsets.UTF_8))) {
            String body = reader.lines().collect(Collectors.joining("\n"));
            message = objectMapper.readTree(body).get("body").asText();
        } catch (IOException e) {
            message = "Failed to decode error response";
            throw new ClientException(500, message);
        }
        throw new ClientException(response.status(), message);
    }
}
