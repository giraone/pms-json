package com.giraone.pms.repository;

import com.giraone.pms.domain.Company;
import com.giraone.pms.domain.Employee;
import io.micrometer.core.annotation.Timed;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.UUID;

@RepositoryRestResource(collectionResourceRel = "employees", path = "employees")
public interface EmployeeRepository extends PagingAndSortingRepository<Employee, UUID> {

    @Timed
    @RestResource(path = "findAllByCompany", rel = "findAllByCompany")
    Page<Employee> findAllByCompany(Company company, Pageable pageable);

    @Timed
    @RestResource(path = "findAllByCompanyAndSurname", rel = "findAllByCompanyAndSurname")
    @Query("SELECT distinct e FROM Employee e" +
        " WHERE e.company = :company" +
        " AND e.surname LIKE :surname")
    Page<Employee> findAllByCompanyAndSurname(
        @Param("company") Company company,
        @Param("surname") String surname,
        Pageable pageable);
}
