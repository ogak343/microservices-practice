package com.example.customer.config;

import com.example.customer.config.exception.CustomException;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import feign.Response;
import feign.codec.Decoder;
import jakarta.persistence.EntityNotFoundException;

import java.io.IOException;
import java.lang.reflect.Type;

public class FeignClientDecoder implements Decoder {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Object decode(Response response, Type type) throws IOException, FeignException {
        if (response.status() >= 200 && response.status() <= 299) {
            return objectMapper.readValue(response.body().asInputStream(), objectMapper.constructType(type));
        } else if (response.status() == 400) {
            throw new CustomException(response.body().toString());
        } else if (response.status() == 404) {
            throw new EntityNotFoundException(response.body().toString());
        }
        throw new RuntimeException("Unexpected response status: " + response.status());
    }
}
