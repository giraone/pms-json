package com.giraone.pms.web.rest;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

/**
 * Utility class for testing REST controllers.
 */
public class TestUtil {

    private static final ObjectMapper mapper = new ObjectMapper();
    private static final TypeReference<HashMap<String, Object>> hashMapTypeRef
        = new TypeReference<HashMap<String, Object>>() {
    };

    static {
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        final JavaTimeModule module = new JavaTimeModule();
        mapper.registerModule(module);
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        // StdDateFormat is ISO8601 since jackson 2.9
        mapper.setDateFormat(new StdDateFormat().withColonInTimeZone(true));
    }

    // Hide constructor
    private TestUtil() {
    }

    public static ObjectMapper getMapper() {
        return mapper;
    }

    /**
     * MediaType for JSON UTF8
     */
    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(
        MediaType.APPLICATION_JSON.getType(),
        MediaType.APPLICATION_JSON.getSubtype(), StandardCharsets.UTF_8);

    /**
     * Convert an object to JSON byte array.
     *
     * @param object the object to convert
     * @return the JSON byte array
     * @throws IOException on any IO error
     */
    static byte[] convertObjectToJsonBytes(Object object)
        throws IOException {
        return mapper.writeValueAsBytes(object);
    }

    /**
     * Convert a JSON string to HashMap<String,Object>.
     *
     * @param jsonString the string to convert
     * @return the HashMap
     * @throws IOException on any IO error
     */
    static HashMap<String, Object> convertToJsonMap(String jsonString)
        throws IOException {

        return mapper.readValue(jsonString, hashMapTypeRef);
    }
}
