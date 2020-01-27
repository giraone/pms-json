package com.giraone.pms.service;

import lombok.*;

import java.io.Serializable;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Generated // exclude from test coverage
public class EmployeeBulkDTO implements Serializable {

    private Long id;
    private String surname;
    private String givenName;
    private String dateOfBirth;
    private String gender;
    private String postalCode;
    private String city;
    private String streetAddress;
    private String companyId;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        EmployeeBulkDTO employeeDTO = (EmployeeBulkDTO) o;
        if (employeeDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), employeeDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }
}
