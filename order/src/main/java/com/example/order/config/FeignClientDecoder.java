package com.example.order.config;

import com.example.order.config.exception.CustomException;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import feign.Response;
import feign.codec.DecodeException;
import feign.codec.Decoder;

import java.io.IOException;
import java.lang.reflect.Type;

public class FeignClientDecoder implements Decoder {

    private final ObjectMapper objectMapper;

    public FeignClientDecoder(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public Object decode(Response response, Type type) throws IOException, DecodeException, FeignException {

        return null;
    }
}
