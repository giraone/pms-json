package com.giraone.pms.service;

import com.giraone.pms.domain.Company;
import com.giraone.pms.domain.Employee;
import com.giraone.pms.domain.enumeration.GenderType;
import com.giraone.pms.repository.CompanyRepository;
import com.giraone.pms.repository.EmployeeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@ConditionalOnProperty(name = "application.fillDatabaseWithSamplesOnStart", havingValue = "true")
@Service
public class DataFillerService {

    private final Logger log = LoggerFactory.getLogger(DataFillerService.class);

    private final CompanyRepository companyRepository;
    private final EmployeeRepository employeeRepository;

    public DataFillerService(CompanyRepository companyRepository, EmployeeRepository employeeRepository) {
        this.companyRepository = companyRepository;
        this.employeeRepository = employeeRepository;
    }

    @PostConstruct
    @Transactional
    public void fillData() {

        long companyCount = companyRepository.count();
        log.info("fillData START: company.count={}", companyCount);

        if (companyCount == 0) {
            Company company = new Company();
            company.setId(UUID.randomUUID());
            company.setName("Test-Company-0001");
            company.setExternalId("test-0001");
            company.setTaxRelevantStateCode("BY");

            final HashMap<String, Object> companyAddress = new HashMap<>();
            company.setCompanyAddress(companyAddress);
            companyAddress.put("postalCode", "91074");
            companyAddress.put("city", "Herzogenaurach");
            companyAddress.put("streetAddress", "Am Industrieweg 1");

            company = companyRepository.save(company);


            Employee employee = new Employee();
            employee.setId(UUID.randomUUID());
            employee.setCompany(company);
            employee.setGivenName("Erwin");
            employee.setSurname("Mustermann");
            employee.setDateOfBirth(LocalDate.of(1965, 12, 21));
            employee.setGender(GenderType.MALE);

            HashMap<String, Object> employeeAddress = new HashMap<>();
            employeeAddress.put("postalCode", "91052");
            employeeAddress.put("city", "Erlangen");
            employeeAddress.put("streetAddress", "Goethestr. 12");
            employee.setPostalAddress(employeeAddress);

            HashMap<String, Object> employeeTaxRelData = new HashMap<>(Map.of(
                "remuneration", Map.of(
                    "payCycleCode", "monthly",
                    "periodType", "month",
                    "amount", 3500.00,
                    "currency", "EUR"
                ),
                "numberOfChildren", 2,
                "denominationCode", "rk",
                "taxClass", "3",
                "maritalStatus", Map.of("status", "married",
                    "effectiveDate", employee.getDateOfBirth().plusDays(364L * 30)
                )
            ));
            employee.setTaxRelevantData(employeeTaxRelData);

            employee = employeeRepository.save(employee);

            log.info("fillData company = {}, employee = {}", company, employee);
        }
    }
}
