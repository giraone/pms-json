package com.giraone.pms.web.rest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.CoreMatchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the PingApiResource REST controller.
 * Basically only to show, how these kind of MockMvc test with "full application context" work.
 *
 * @see PingApiResource
 */
@SpringBootTest
@AutoConfigureMockMvc
class PingApiResourceIntTest {

    @Autowired
    private MockMvc mockMvc;

    @DisplayName("Test GET /api/ping (full context(")
    @Test
    void assertThat_ping_works() throws Exception {

        // act/assert
        mockMvc.perform(get("/api/ping")
            .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE)) // Normally not necessary
            .andExpect(content().string(containsString("\"OK\""))) // Normally not necessary
            .andExpect(jsonPath("$.status").value("OK"));
    }
}
