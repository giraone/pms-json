package com.giraone.pms.web.rest;

import com.giraone.pms.domain.Company;
import com.giraone.pms.domain.Employee;
import com.giraone.pms.domain.enumeration.GenderType;
import com.giraone.pms.repository.CompanyRepository;
import com.giraone.pms.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Sql(value = {"/schema-h2-create.sql" /*, "/import.sql"*/}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/schema-h2-drop.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@SpringBootTest
class SpringDataRestResourcesIntTest {

    private static final String BASE_HAL_URL = "http://localhost";

    private final Logger log = LoggerFactory.getLogger(SpringDataRestResourcesIntTest.class);

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private EntityManager em; // Needed to flush test data before some tests

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        this.mockMvc = MockMvcBuilders
            .webAppContextSetup(wac)
            //.apply(springSecurity())
            .build();
    }

    @DisplayName("Test GET /api/companies")
    @Test
    @Transactional
    void assertThat_get_companies_works() throws Exception {

        // arrange
        Company testCompany = this.storeAndReturnTestCompany();

        // act/assert
        mockMvc.perform(get("/api/companies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$._embedded.companies[0]._links.self.href")
                .value(BASE_HAL_URL + "/api/companies/" + testCompany.getId()))
            .andExpect(jsonPath("$._embedded.companies[0].externalId")
                .value(testCompany.getExternalId()))
            .andExpect(jsonPath("$._embedded.companies[0].name")
                .value(testCompany.getName()))
        ;
    }

    @DisplayName("Test POST /api/companies")
    @Test
    @Transactional
    void assertThat_post_company_works() throws Exception {

        // arrange
        long countBefore = companyRepository.count();

        final Company company = new Company();
        company.setId(UUID.randomUUID());
        company.setName("Test-Company-0002");
        company.setExternalId("test-0002");
        company.setTaxRelevantStateCode("BY");
        final HashMap<String, Object> companyAddress = new HashMap<>();
        company.setCompanyAddress(companyAddress);
        companyAddress.put("postalCode", "91052");
        companyAddress.put("city", "Erlangen");
        companyAddress.put("streetAddress", "Goethestr. 3");

        // act/assert
        mockMvc.perform(post("/api/companies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(company)))
            .andDo(print())
            .andExpect(status().isCreated())
            .andExpect(header().string("Location", BASE_HAL_URL + "/api/companies/" + company.getId()))
        ;

        // assert
        long countAfter = companyRepository.count();
        assertThat(countAfter).isEqualTo(countBefore + 1);
    }

    //------------------------------------------------------------------------------------------------------------------

    @DisplayName("Test GET /api/employees")
    @Test
    @Transactional
    void assertThat_get_employee_works() throws Exception {

        // arrange
        Company testCompany = this.storeAndReturnTestCompany();

        Employee employee = new Employee();
        employee.setId(UUID.randomUUID());
        employee.setCompany(testCompany);
        employee.setSurname("Doe");
        employee.setGivenName("John");
        employee.setDateOfBirth(LocalDate.of(1982, 11, 21));
        employee.setGender(GenderType.MALE);
        employee.setPostalAddress(TestUtil.convertToJsonMap("{" +
            "\"postalCode\": \"91126\"," +
            "\"city\": \"Schwabach\"," +
            "\"streetAddress\": \"Am Weg 2\"" +
            "}"));
        employeeRepository.save(employee);

        // act/assert
        mockMvc.perform(get("/api/employees")
            .contentType(TestUtil.APPLICATION_JSON_UTF8))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$._embedded.employees[0]._links.self.href")
                .value(BASE_HAL_URL + "/api/employees/" + employee.getId()))
            .andExpect(jsonPath("$._embedded.employees[0].givenName")
                .value(employee.getGivenName()))
            .andExpect(jsonPath("$._embedded.employees[0].surname")
                .value(employee.getSurname()))
            .andExpect(jsonPath("$._embedded.employees[0].dateOfBirth")
                .value(employee.getDateOfBirth().toString()))
            .andExpect(jsonPath("$._embedded.employees[0].gender")
                .value(employee.getGender().name()))
            .andExpect(jsonPath("$._embedded.employees[0].postalAddress")
                .value(employee.getPostalAddress()))
        ;
    }

    @DisplayName("Test POST /api/employees")
    @Test
    @Disabled
    @Transactional
    void assertThat_post_employee_works() throws Exception {

        // arrange
        Company testCompany = this.storeAndReturnTestCompany();
        long countBeforeCompany = companyRepository.count();
        long countBefore = employeeRepository.count();

        Employee employee = new Employee();
        employee.setId(UUID.randomUUID());
        employee.setCompany(testCompany);
        employee.setSurname("Doe");
        employee.setGivenName("Jane");
        employee.setDateOfBirth(LocalDate.of(1982, 11, 21));
        employee.setGender(GenderType.FEMALE);
        employee.setPostalAddress(TestUtil.convertToJsonMap("{" +
            "\"postalCode\": \"93233\"," +
            "\"city\": \"Schwabach\"," +
            "\"streetAddress\": \"Am Weg 2\"" +
            "}"));
        final HashMap<String, Object> taxRelevantData = new HashMap<>();
        employee.setTaxRelevantData(taxRelevantData);

        // act/assert
        byte[] payLoad = TestUtil.convertObjectToJsonBytes(employee);
        log.info("POST employees payload = {}", new String(payLoad));
        mockMvc.perform(post("/api/employees")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(payLoad))
            .andDo(print())
            .andExpect(status().isCreated())
        //.andExpect(header().string("Location", BASE_HAL_URL + "/api/employees/" + employee.getId()))
        ;

        // assert
        em.flush();
        long countAfter = employeeRepository.count();
        assertThat(countAfter).isEqualTo(countBefore + 1);
        long countAfterCompany = companyRepository.count();
        assertThat(countAfterCompany).isEqualTo(countBeforeCompany);
    }

    //------------------------------------------------------------------------------------------------------------------

    private Company storeAndReturnTestCompany() {

        // This is the company stored in import.sql
        // return companyRepository.findById(UUID.fromString("3b966514-2c6e-4199-9989-3963012df83c"));

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

        company = companyRepository.save(company);
        em.flush();
        return company;
    }

}