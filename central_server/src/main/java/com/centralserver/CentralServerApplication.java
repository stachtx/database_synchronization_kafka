package com.centralserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication(exclude = KafkaAutoConfiguration.class)
@EnableScheduling
@EnableTransactionManagement
public class CentralServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(CentralServerApplication.class, args);
    }
}
