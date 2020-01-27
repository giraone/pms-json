package com.giraone.pms.repository;

import com.giraone.pms.domain.Company;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.Optional;
import java.util.UUID;

@RepositoryRestResource(collectionResourceRel = "companies", path = "companies")
public interface CompanyRepository extends PagingAndSortingRepository<Company, UUID> {

    @RestResource(path = "nameStartsWith", rel = "nameStartsWith")
    Page findByNameStartsWith(@Param("name") String name, Pageable p);

    Optional<Company> findOneByExternalId(String externalId);
}