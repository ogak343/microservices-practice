package com.example.order.config.helpers;

import com.example.order.config.exception.ClientException;
import feign.FeignException;
import feign.Response;
import feign.codec.Decoder;
import org.springframework.http.HttpStatus;

import java.lang.reflect.Type;


public class FeignClientDecoder implements Decoder {

    @Override
    public Object decode(Response response, Type type) throws FeignException {

        if (HttpStatus.valueOf(response.status()).is2xxSuccessful()) {
            return response;
        }

        throw new ClientException(response.status(), response.body().toString());
    }
}
