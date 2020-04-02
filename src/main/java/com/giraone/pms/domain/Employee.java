package com.giraone.pms.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.giraone.pms.domain.enumeration.GenderType;
import com.giraone.pms.repository.conversion.MapConverter;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.UUID;

@Entity
@Table(name = "employee")
@Data
@NoArgsConstructor
@ToString
//@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
public class Employee implements Serializable {

    @Id
    //@Type(type = "pg-uuid") // Only, if hibernate DDL generation is used
    private UUID id;

    @Version
    private Timestamp version;

    @NotNull
    @Column(name = "surname", nullable = false)
    private String surname;

    @Column(name = "given_name")
    private String givenName;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private GenderType gender;

    @Column(name = "postal_address")
    @Convert(converter = MapConverter.class)
    //@Type(type = "jsonb")
    private HashMap<String, Object> postalAddress;

    @Column(name = "tax_relevant_data")
    @Convert(converter = MapConverter.class)
    //@Type(type = "jsonb")
    @Basic(fetch = FetchType.LAZY)
    private HashMap<String, Object> taxRelevantData;

    @ManyToOne(optional = false)
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_company"))
    @NotNull
    @JsonIgnoreProperties("employees") // Do not fetch employees again
    private Company company;
}
