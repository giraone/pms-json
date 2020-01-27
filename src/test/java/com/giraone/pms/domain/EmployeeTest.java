package com.giraone.pms.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.giraone.pms.domain.enumeration.GenderType;
import com.giraone.pms.web.rest.TestUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class EmployeeTest {

    private final Logger log = LoggerFactory.getLogger(EmployeeTest.class);

    private final ObjectMapper objectMapper = TestUtil.getMapper();

    @DisplayName("Test JSON Serde of employee")
    @Test
    void assertThat_employee_can_be_deserialized() throws Exception {

        final Company company = new Company();
        company.setId(UUID.randomUUID());
        company.setExternalId("test-0001");
        company.setId(UUID.randomUUID());
        company.setName("Test 0001 GmbH");
        final HashMap<String, Object> companyAddress = new HashMap<>();
        company.setCompanyAddress(companyAddress);
        companyAddress.put("postalCode", "91074");
        companyAddress.put("city", "Herzogenaurach");
        companyAddress.put("streetAddress", "Am Industrieweg 1");

        final Employee employee = new Employee();
        employee.setId(UUID.randomUUID());
        employee.setCompany(company);
        employee.setSurname("Doe");
        employee.setGivenName("John");
        employee.setDateOfBirth(LocalDate.of(1982, 11, 21));
        employee.setGender(GenderType.MALE);
        final HashMap<String, Object> employeeAddress = new HashMap<>();
        employee.setPostalAddress(employeeAddress);
        employeeAddress.put("postalCode", "91126");
        employeeAddress.put("city", "Schwabach");
        employeeAddress.put("streetAddress", "Am Weg 2");

        final String jsonString = objectMapper.writeValueAsString(employee);
        log.info("Employee as JSON = {}", jsonString);
        final Employee actualEmployee = objectMapper.readValue(jsonString, Employee.class);
        assertThat(actualEmployee).isEqualToComparingFieldByField(employee);
        final String actualJsonString = objectMapper.writeValueAsString(employee);
        assertThat(actualJsonString).isEqualTo(jsonString);
    }
}