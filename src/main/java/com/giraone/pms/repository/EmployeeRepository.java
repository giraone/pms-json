package com.giraone.pms.repository;

import com.giraone.pms.domain.Company;
import com.giraone.pms.domain.Employee;
import io.micrometer.core.annotation.Timed;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.UUID;

@RepositoryRestResource(collectionResourceRel = "employees", path = "employees")
public interface EmployeeRepository extends PagingAndSortingRepository<Employee, UUID>,
    QuerydslPredicateExecutor<Employee> {

    @Timed
    @RestResource(path = "findAllByCompany", rel = "findAllByCompany")
    Page<Employee> findAllByCompany(Company company, Pageable pageable);

    @Timed
    @RestResource(path = "findAllByCompanyExternalId", rel = "findAllByCompanyExternalId")
    @Query("SELECT DISTINCT e FROM Employee e WHERE e.company.externalId = :companyExternalId")
    Page<Employee> findAllByCompanyExternalId(
        @Param("companyExternalId") String companyExternalId,
        Pageable pageable);

    @Timed
    @RestResource(path = "findAllByCompanyExternalIdAndSurname", rel = "findAllByCompanyExternalIdAndSurname")
    @Query("SELECT DISTINCT e FROM Employee e" +
        " WHERE e.company.externalId = :companyExternalId" +
        " AND e.surname LIKE :surname")
    Page<Employee> findAllByCompanyExternalIdAndSurname(
        @Param("companyExternalId") String companyExternalId,
        @Param("surname") String surname,
        Pageable pageable);

    @Timed
    @RestResource(path = "findAllByNumberOfChildren", rel = "findAllByNumberOfChildren")
    @Query(value = "SELECT * FROM Employee e WHERE e.tax_relevant_data @> '{\"numberOfChildren\": ?1}'", nativeQuery = true)
    Page<Employee> findAllByNumberOfChildren(int numberOfChildren, Pageable pageable);

    //------------------------------------------------------------------------------------------------------------------

}
