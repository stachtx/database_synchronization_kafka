package com.database.integration.central;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@EnableScheduling
@EnableTransactionManagement
@SpringBootApplication(exclude = KafkaAutoConfiguration.class)
@ComponentScan(basePackages = {"com.database.integration.core"})
@EntityScan("com.database.integration.core.model")
public class CentralServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(CentralServerApplication.class, args);
    }
}
