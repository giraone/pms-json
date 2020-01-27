package com.giraone.pms.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.giraone.pms.web.rest.TestUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class CompanyTest {

    private final Logger log = LoggerFactory.getLogger(CompanyTest.class);

    private final ObjectMapper objectMapper = TestUtil.getMapper();

    @DisplayName("Test JSON Serde of company")
    @Test
    void assertThat_company_can_be_deserialized() throws Exception {

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

        final String jsonString = objectMapper.writeValueAsString(company);
        log.info("Company as JSON = {}", jsonString);
        final Company actualCompany = objectMapper.readValue(jsonString, Company.class);
        assertThat(actualCompany).isEqualToComparingFieldByField(company);
        final String actualJsonString = objectMapper.writeValueAsString(company);
        assertThat(actualJsonString).isEqualTo(jsonString);
    }
}