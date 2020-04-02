package com.giraone.pms.repository.conversion;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class MapConverterTest {

    private MapConverter cut = new MapConverter();

    private Map<String,Object> map = new HashMap<>();

    {
        Map<String,Object> subMapC = new HashMap<>();

        Map<String,Object> subMapC0 = new HashMap<>();
        Map<String,Object> subMapC1 = new HashMap<>();

        map.put("key-a", "value-a");
        map.put("key-b", 1);
        map.put("key-c", subMapC);
        subMapC.put("key-c.a", "x");
        subMapC.put("key-c.b", true);

        subMapC0.put("key-c.c.0", "one");
        subMapC1.put("key-c.c.1", "one");
        subMapC.put("key-c.c", Arrays.asList(subMapC0, subMapC1));
    }

    @Test
    void convertToDatabaseColumn_success() {

        String result = cut.convertToDatabaseColumn(map);
        assertThat(result).contains("\"key-a\":\"value-a\"");
        assertThat(result).contains("\"key-b\":1");
        assertThat(result).contains("\"key-c\":{");
        assertThat(result).contains("\"key-c.a\":\"x\"");
        assertThat(result).contains("\"key-c.b\":true");
        assertThat(result).contains("\"key-c.c\":[{");
    }

    @Test
    void convertToDatabaseColumn_failure() {

        Map<String, Object> badMap = new HashMap<>();
        badMap.put("key1", "value");
        badMap.put("key2", new Object());
        String result = cut.convertToDatabaseColumn(badMap);
        assertThat(result).isNull();
    }

    @Test
    void convertToEntityAttribute_success() {

        String input = "{\"key-a\":\"value-a\",\"key-b\":1,\"key-c\":{\"key-c.a\":\"x\",\"key-c.b\":true,\"key-c.c\":[{\"key-c.c.0\":\"one\"},{\"key-c.c.1\":\"two\"}]}}";
        Map<String, Object> result = cut.convertToEntityAttribute(input);
        assertThat(result.get("key-a")).isEqualTo("value-a");
        assertThat(result.get("key-b")).isEqualTo(1);
        assertThat(result.get("key-c")).isInstanceOf(Map.class);
        @SuppressWarnings("unchecked")
        Map<String,Object> keyC = (Map<String,Object>) result.get("key-c");
        assertThat(keyC.get("key-c.a")).isEqualTo("x");
        assertThat(keyC.get("key-c.b")).isEqualTo(true);
        assertThat(keyC.get("key-c.c")).isInstanceOf(List.class);
        @SuppressWarnings("unchecked")
        List<Map<String,Object>> keyCC = (List<Map<String,Object>>) keyC.get("key-c.c");
        assertThat(keyCC.get(0).get("key-c.c.0")).isEqualTo("one");
        assertThat(keyCC.get(1).get("key-c.c.1")).isEqualTo("two");
    }

    @Test
    void convertToEntityAttribute_failure() {

        //TODO: Converter is lax - see ...
        //String badInput = "{\"key-a\":\"value-a\",\"key-b\": [0, true, \"1\"]}";
        String badInput = "{\"key-a\":\"value-a\"";
        Map<String, Object> result = cut.convertToEntityAttribute(badInput);
        assertThat(result).isNull();
    }

    @Test
    void viceVersa_success() {

        Map<String, Object> result = cut.convertToEntityAttribute(cut.convertToDatabaseColumn(map));
        assertThat(result).usingRecursiveComparison().isEqualTo(map);
    }
}