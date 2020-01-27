package com.giraone.pms.web.rest;

import com.giraone.pms.domain.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.util.Collections;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Integration test class for the EmployeeBulkResource REST controller.
 *
 * @see EmployeeBulkResource
 */

@SpringBootTest
@Sql(value = {"/schema-h2-create.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/schema-h2-drop.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class EmployeeBulkResourceIntTest {

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        this.mockMvc = MockMvcBuilders
            .webAppContextSetup(wac)
            //.apply(springSecurity())
            .build();
    }

    @DisplayName("Test bulk import with PUT /api/employee-list")
    @Test
    @Transactional
    void assertThat_simple_bulk_import_works() throws Exception {

        // arrange
        Employee employee = new Employee();
        employee.setSurname("Doe");
        employee.setGivenName("John");
        List<Employee> employeeList = Collections.singletonList(employee);

        // act/assert
        mockMvc.perform(put("/api/employee-list")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(employeeList)))
            //.andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").value(employeeList.size()));
    }
}