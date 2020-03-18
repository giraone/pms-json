package com.giraone.pms.service;

import com.giraone.pms.domain.Company;
import com.giraone.pms.domain.Employee;
import com.giraone.pms.domain.enumeration.GenderType;
import com.giraone.pms.repository.CompanyRepository;
import com.giraone.pms.repository.EmployeeRepository;
import io.micrometer.core.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Service
public class EmployeesBulkService {

    private final static AtomicLong EMPLOYEE_COUNT = new AtomicLong(0L);
    private final static AtomicLong DURATION = new AtomicLong(0L);

    private final Random random = new Random();

    private final Logger log = LoggerFactory.getLogger(EmployeesBulkService.class);

    private final EmployeeRepository employeeRepository;
    private final CompanyRepository companyRepository;

    public EmployeesBulkService(EmployeeRepository employeeRepository, CompanyRepository companyRepository) {
        this.employeeRepository = employeeRepository;
        this.companyRepository = companyRepository;
    }

    /**
     * Save a list of employees in one transaction and create companies dynamically.
     *
     * @param employeeDTOList the entity list to save
     * @return the number of saved employees
     */
    @Timed
    @Transactional
    public int save(List<EmployeeBulkDTO> employeeDTOList) {

        final long start = System.currentTimeMillis();
        final int size = employeeDTOList.size();
        final List<Employee> employees = employeeDTOList.stream()
            // Employee-DTO to Employee-Entity
            .map(EmployeesBulkService.this::map)
            // Employee without Company to employee with company
            .map(employee -> { // map with side effects!
                // Store the company, if it doesn't yet exist
                final String externalId = employee.getCompany().getExternalId();
                final Optional<Company> optionalCompany = this.companyRepository.findOneByExternalId(externalId);
                if (optionalCompany.isPresent()) {
                    employee.setCompany(optionalCompany.get());
                } else {
                    // company does not yet exist, so insert company with name and address of first user
                    Company company = employee.getCompany();
                    company.setId(UUID.randomUUID());
                    company.setName(employee.getSurname() + " GmbH");
                    final HashMap<String, Object> companyAddress = new HashMap<>();
                    company.setCompanyAddress(companyAddress);
                    companyAddress.put("postalCode", employee.getPostalAddress().get("postalCode"));
                    companyAddress.put("city", employee.getPostalAddress().get("city"));
                    companyAddress.put("streetAddress", employee.getPostalAddress().get("streetAddress"));
                    company = this.companyRepository.save(company);
                    employee.setCompany(company);
                }
                return employee;
            })
            .collect(Collectors.toList());

        this.employeeRepository.saveAll(employees);
        final long duration = System.currentTimeMillis() - start;
        final long totalCount = EMPLOYEE_COUNT.addAndGet(size);
        final long totalDuration = DURATION.addAndGet(duration);
        log.info("Added {} employees in {} msec, total {} in {} msec, RPS = {}",
            size, duration, totalCount, totalDuration, 1000L * totalCount / totalDuration);
        return size;
    }

    private Employee map(EmployeeBulkDTO dto) {
        final Employee employee = new Employee();
        final HashMap<String, Object> employeeAddress = new HashMap<>();
        final Company company = new Company();

        employee.setCompany(company);
        employee.setPostalAddress(employeeAddress);

        company.setExternalId(dto.getCompanyId());

        employee.setId(UUID.randomUUID());
        if (dto.getDateOfBirth() != null) {
            employee.setDateOfBirth(LocalDate.parse(dto.getDateOfBirth(), DateTimeFormatter.BASIC_ISO_DATE));
        }
        employee.setGender(GenderType.fromString(dto.getGender()));
        employee.setSurname(dto.getSurname());
        employee.setGivenName(dto.getGivenName());

        employeeAddress.put("postalCode", dto.getPostalCode());
        employeeAddress.put("city", dto.getCity());
        employeeAddress.put("streetAddress", dto.getStreetAddress());

        boolean married = employee.getDateOfBirth() != null && random.nextBoolean();
        HashMap<String, Object> employeeTaxRelData = new HashMap<>(Map.of(
            "remuneration", Arrays.asList(
                Map.of(
                    "payCycleCode", "monthly",
                    "periodType", "month",
                    "amount", random.nextInt(100) * 100,
                    "currency", "EUR"
                ),
                Map.of(
                    "payCycleCode", "holiday-pay",
                    "periodType", "year",
                    "amount", random.nextInt(50) * 100,
                    "currency", "EUR"
                )
            ),
            "numberOfChildren", random.nextInt(3),
            "taxClass", random.nextBoolean() ? "3" : "5",
            "denominationCode", "rk",
            "maritalStatus", married ?
                Map.of("status", "married",
                    "effectiveDate", employee.getDateOfBirth().plusDays(365L * 20 + random.nextInt(3650))
                ) :
                Map.of("status", "single")
        ));

        employee.setTaxRelevantData(employeeTaxRelData);

        return employee;
    }
}
