package com.giraone.pms.repository.conversion;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.AttributeConverter;
import java.io.IOException;
import java.util.Map;

public class HashMapConverter implements AttributeConverter<Map<String, Object>, String> {

    private static final Logger logger = LoggerFactory.getLogger(HashMapConverter.class);

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(Map<String, Object> customerInfo) {

        String jsonString = null;
        try {
            jsonString = objectMapper.writeValueAsString(customerInfo);
        } catch (final JsonProcessingException e) {
            logger.error("JSON writing error", e);
        }

        return jsonString;
    }

    @Override
    public Map<String, Object> convertToEntityAttribute(String jsonString) {

        Map<String, Object> customerInfo = null;
        try {
            customerInfo = objectMapper.readValue(jsonString, Map.class);
        } catch (final IOException e) {
            logger.error("JSON reading error", e);
        }

        return customerInfo;
    }

}