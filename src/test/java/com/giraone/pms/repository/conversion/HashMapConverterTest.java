package com.giraone.pms.repository.conversion;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class HashMapConverterTest {

    private HashMapConverter cut = new HashMapConverter();

    private HashMap<String,Object> map = new HashMap<>(Map.of(
        "key-a", "value-a",
        "key-b", 1,
        "key-c", Map.of(
            "key-c.a", "x",
            "key-c.b", true,
            "key-c.c", Arrays.asList(
                Map.of("key-c.c.0", "one"),
                Map.of("key-c.c.1", "two")
            )
        )
    ));

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

        HashMap<String, Object> badMap = new HashMap<>();
        badMap.put("key", "value");
        badMap.put("key", new Object());
        String result = cut.convertToDatabaseColumn(badMap);
        assertThat(result).isNull();
    }

    @Test
    void convertToEntityAttribute_success() {

        String input = "{\"key-a\":\"value-a\",\"key-b\":1,\"key-c\":{\"key-c.a\":\"x\",\"key-c.b\":true,\"key-c.c\":[{\"key-c.c.0\":\"one\"},{\"key-c.c.1\":\"two\"}]}}";
        Map<String, Object> result = cut.convertToEntityAttribute(input);
        assertThat(result.get("key-a")).isEqualTo("value-a");
        assertThat(result.get("key-b")).isEqualTo(1);
        assertThat(result.get("key-c")).isInstanceOf(HashMap.class);
        HashMap<String,Object> keyc = (HashMap<String,Object>) result.get("key-c");
        assertThat(keyc.get("key-c.a")).isEqualTo("x");
        assertThat(keyc.get("key-c.b")).isEqualTo(true);
        assertThat(keyc.get("key-c.c")).isInstanceOf(List.class);
        List<HashMap<String,Object>> keycc = (List<HashMap<String,Object>>) keyc.get("key-c.c");
        assertThat(keycc.get(0).get("key-c.c.0")).isEqualTo("one");
        assertThat(keycc.get(1).get("key-c.c.1")).isEqualTo("two");
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