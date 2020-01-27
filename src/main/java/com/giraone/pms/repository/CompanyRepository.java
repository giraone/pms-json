package com.giraone.pms.repository;

import com.giraone.pms.domain.Company;
import com.giraone.pms.domain.QCompany;
import com.querydsl.core.types.dsl.StringPath;
import io.micrometer.core.annotation.Timed;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.Optional;
import java.util.UUID;

@RepositoryRestResource(collectionResourceRel = "companies", path = "companies")
public interface CompanyRepository extends PagingAndSortingRepository<Company, UUID>,
    QuerydslPredicateExecutor<Company> {

    @RestResource(path = "nameStartsWith", rel = "nameStartsWith")
    Page findByNameStartsWith(@Param("name") String name, Pageable p);

    Optional<Company> findOneByExternalId(String externalId);

    @Timed
    @RestResource(path = "findAllByPostalCode", rel = "findAllByPostalCode")
    @Query(value = "SELECT * FROM Company c WHERE c.company_address->>'postalCode' = :postalCode", nativeQuery = true)
    Page<Company> findAllByPostalCode(@Param("postalCode") String postalCode, Pageable pageable);

    //------------------------------------------------------------------------------------------------------------------

    /**
     * Tweak the Querydsl binding if collection resources are filtered.
     */
    default void customize(QuerydslBindings bindings, QCompany company) {

        bindings.bind(company.name).first((path, value) -> path.startsWith(value));
        bindings.bind(String.class).first((StringPath path, String value) -> path.contains(value));
    }
}