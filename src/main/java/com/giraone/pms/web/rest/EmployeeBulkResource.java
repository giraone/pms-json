package com.giraone.pms.web.rest;

import com.giraone.pms.service.EmployeeBulkDTO;
import com.giraone.pms.service.EmployeesBulkService;
import io.micrometer.core.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST controller for managing employee bulk imports
 */
@RestController
@RequestMapping("/api")
public class EmployeeBulkResource {

    private final Logger log = LoggerFactory.getLogger(EmployeeBulkResource.class);

    private final EmployeesBulkService employeeBulkService;

    public EmployeeBulkResource(EmployeesBulkService employeeBulkService) {
        this.employeeBulkService = employeeBulkService;
    }

    @PutMapping("/employee-list")
    @Timed
    public ResponseEntity<Integer> insert(@RequestBody List<EmployeeBulkDTO> employees) {

        if (log.isDebugEnabled()) {
            log.debug("EmployeeBulkResource.insert employees.size={}", employees.size());
        }
        final int count = employeeBulkService.save(employees);
        return ResponseEntity.ok().body(count);
    }
}
