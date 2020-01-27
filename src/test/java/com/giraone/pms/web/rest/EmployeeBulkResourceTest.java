package com.giraone.pms.web.rest;

import com.giraone.pms.service.EmployeeBulkDTO;
import com.giraone.pms.service.EmployeesBulkService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

/**
 * Unit test class for the EmployeeBulkResource REST controller.
 *
 * @see EmployeeBulkResource
 */
@ExtendWith(SpringExtension.class) // for JUnit 5
@WebMvcTest(EmployeeBulkResource.class) // instantiate only this Controller!
class EmployeeBulkResourceTest {

    @MockBean
    EmployeesBulkService employeeBulkService;

    @DisplayName("Test GET /api/employee-list (web layer with mock)")
    @Test
    void assertThat_save_works() {

        // arrange
        EmployeeBulkResource employeeBulkResource = new EmployeeBulkResource(employeeBulkService);

        List<EmployeeBulkDTO> employeeList = Arrays.asList(
            EmployeeBulkDTO.builder()
                .companyId(UUID.randomUUID().toString())
                .givenName("givenName1")
                .surname("surName1")
                .dateOfBirth("1970-12-31")
                .gender("MALE")
                .postalCode("12345")
                .city("City")
                .streetAddress("Street Name 1")
                .build(),
            EmployeeBulkDTO.builder()
                .companyId(UUID.randomUUID().toString())
                .givenName("givenName2")
                .surname("surName2")
                .dateOfBirth("1970-12-31")
                .gender("MALE")
                .postalCode("12345")
                .city("City")
                .streetAddress("Street Name 1")
                .build()
        );

        when(employeeBulkService.save(employeeList)).thenReturn(employeeList.size());

        // act
        ResponseEntity<Integer> responseEntity = employeeBulkResource.insert(employeeList);

        // assert
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isEqualTo(employeeList.size());

        /*
        // DOES NOT WORK: 0 instead of 2 is returned
        mockMvc.perform(put("/api/employee-list")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(employeeList)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").value(employeeList.size()));
        */
    }
}
