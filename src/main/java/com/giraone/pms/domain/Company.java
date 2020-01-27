package com.giraone.pms.domain;

import com.giraone.pms.repository.conversion.HashMapConverter;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.UUID;

/**
 * The Company entity.
 */

@Entity
@Table(name = "company")
@Data
@NoArgsConstructor
@ToString
public class Company implements Serializable {

    @Id
    //@Type(type = "pg-uuid") // Only, if hibernate DDL generation is used
    private UUID id;

    @Version
    private Timestamp version;

    @NotNull
    @Column(name = "external_id", nullable = false, unique = true)
    private String externalId;

    @Column(name = "name")
    private String name;

    @Column(name = "tax_rel_state_code")
    private String taxRelevantStateCode;


    @Column(name = "company_address")
    @Convert(converter = HashMapConverter.class)
    private HashMap<String, Object> companyAddress;
}
