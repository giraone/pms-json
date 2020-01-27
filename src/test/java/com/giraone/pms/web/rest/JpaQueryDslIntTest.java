package com.giraone.pms.web.rest;

import com.giraone.pms.PmsApplication;
import com.giraone.pms.domain.Company;
import com.giraone.pms.repository.CompanyRepository;
import com.giraone.pms.repository.queries.CompanyPredicatesBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { PmsApplication.class })
@Transactional
@Rollback
public class JpaQueryDslIntTest {

    @Autowired
    private CompanyRepository companyRepository;

    private Company company1;
    private Company company2;

    @BeforeEach
    public void init() {
        {
            Company company = new Company();
            company.setId(UUID.fromString("4b966514-2c6e-4199-9989-3963012df83c"));
            company.setName("Test-Company BBBB");
            company.setExternalId("test-bbbb");
            company.setTaxRelevantStateCode("BY");
            final HashMap<String, Object> companyAddress = new HashMap<>();
            company.setCompanyAddress(companyAddress);
            companyAddress.put("postalCode", "91074");
            companyAddress.put("city", "Herzogenaurach");
            companyAddress.put("streetAddress", "Am Industrieweg 1");
            company1 = companyRepository.save(company);
        }
        {
            Company company = new Company();
            company.setId(UUID.fromString("5b966514-2c6e-4199-9989-3963012df83c"));
            company.setName("Test-Company CCCC");
            company.setExternalId("test-cccc");
            company.setTaxRelevantStateCode("BY");
            final HashMap<String, Object> companyAddress = new HashMap<>();
            company.setCompanyAddress(companyAddress);
            companyAddress.put("postalCode", "91074");
            companyAddress.put("city", "Herzogenaurach");
            companyAddress.put("streetAddress", "Am Industrieweg 1");
            company2 = companyRepository.save(company);
        }
    }

    @AfterEach
    private void clear() {
        companyRepository.deleteAll();
    }

    @Test
    void assertThat_queryCompanyByName_works() {

        CompanyPredicatesBuilder builder = new CompanyPredicatesBuilder().with("name", ":", "Test-Company CCCC");
        Iterable<Company> results = companyRepository.findAll(builder.build());
        assertThat(results).hasSize(1);
        assertThat(results).contains(company2);
    }

    @Test
    void assertThat_queryCompanyByTaxRelevantStateCode_works() {

        CompanyPredicatesBuilder builder = new CompanyPredicatesBuilder().with("taxRelevantStateCode", ":", "BY");
        Iterable<Company> results = companyRepository.findAll(builder.build());
        assertThat(results).containsExactlyInAnyOrder(company1, company2);
    }
}
