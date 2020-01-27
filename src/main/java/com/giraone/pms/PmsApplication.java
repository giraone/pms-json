package com.giraone.pms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;

@SpringBootApplication
public class PmsApplication implements ApplicationListener<ApplicationReadyEvent> {

    private final Logger log = LoggerFactory.getLogger(PmsApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(PmsApplication.class, args);
    }

    @Override
    public void onApplicationEvent(final ApplicationReadyEvent event) {

        log.info("PmsApplication STARTED");
    }
}
