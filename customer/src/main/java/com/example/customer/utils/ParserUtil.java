package com.example.customer.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ParserUtil {

    public static String parseToString(String resp, String accessToken) {

        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode jsonNode = mapper.readTree(resp);
            return jsonNode.get(accessToken).asText();
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to parse response body", e);
        }
    }
}
