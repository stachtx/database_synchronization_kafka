package com.database.integration.central.kafka;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaTopicConfig {

    @Value(value = "${kafka.bootstrapAddress}")
    private String bootstrapAddress;

    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        return new KafkaAdmin(configs);
    }
    //TODO Add topics
    @Bean
    public NewTopic centralTopicUser() {
        return new NewTopic("central-topic-user", 1, (short) 1);
    }

    @Bean
    public NewTopic unitTopicUser() {
        return new NewTopic("unit-topic-user", 1, (short) 1);
    }

    @Bean
    public NewTopic unitTopicProduct() {
        return new NewTopic("unit-topic-product", 1, (short) 1);
    }

    @Bean
    public NewTopic unitTopicProductType() {
        return new NewTopic("unit-topic-product-type", 1, (short) 1);
    }

    @Bean
    public NewTopic unitTopicDepartment() {
        return new NewTopic("unit-topic-department", 1, (short) 1);
    }
}
