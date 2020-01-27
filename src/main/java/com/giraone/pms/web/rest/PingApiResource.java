package com.giraone.pms.web.rest;

import com.giraone.pms.service.PingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * REST controller for simple REST test.
 */
@RestController
@RequestMapping("/api")
public class PingApiResource {

    private final Logger log = LoggerFactory.getLogger(PingApiResource.class);

    private final PingService pingService;

    public PingApiResource(PingService pingService) {
        this.pingService = pingService;
    }

    /**
     * GET  /api/ping : ping method to check, whether application works
     *
     * @return the ResponseEntity with status 200 (OK) and { "status"; "OK" } as body data
     */
    @GetMapping("/ping")
    public ResponseEntity<Map<String, String>> getPingStatus() {
        log.debug("PingApiResource.getPingStatus called");
        HashMap<String, String> ret = new HashMap<>();
        ret.put("status", pingService.getOkString());
        return ResponseEntity.ok(ret);
    }
}
