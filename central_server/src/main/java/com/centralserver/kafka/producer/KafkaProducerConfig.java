package com.centralserver.kafka.producer;

import com.centralserver.model.Department;
import com.centralserver.model.products.Product;
import com.centralserver.model.products.ProductType;
import com.centralserver.model.users.User;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.HashMap;
import java.util.Map;

@EnableAsync
@Configuration
public class KafkaProducerConfig {

    @Value(value = "${kafka.bootstrapAddress}")
    private String bootstrapAddress;

    @Bean
    public Map<String, Object> producerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return props;
    }

    //Product
    @Bean
    public ProducerFactory<String, Product> productProducerFactory() {
        JsonSerializer<Product> serializer = new JsonSerializer<>();
        serializer.setUseTypeMapperForKey(true);
        return new DefaultKafkaProducerFactory<>(producerConfigs(), new StringSerializer(), serializer);
    }

    @Bean
    public KafkaTemplate<String, Product> productKafkaTemplate() {
        return new KafkaTemplate<>(productProducerFactory());
    }

    //ProductType
    @Bean
    public ProducerFactory<String, ProductType> productTypeProducerFactory() {
        JsonSerializer<ProductType> serializer = new JsonSerializer<>();
        serializer.setUseTypeMapperForKey(true);
        return new DefaultKafkaProducerFactory<>(producerConfigs(), new StringSerializer(), serializer);
    }

    @Bean
    public KafkaTemplate<String, ProductType> productTypeKafkaTemplate() {
        return new KafkaTemplate<>(productTypeProducerFactory());
    }

    // User
    @Bean
    public ProducerFactory<String, User> userProducerFactory() {
        JsonSerializer<User> serializer = new JsonSerializer<>();
        serializer.setUseTypeMapperForKey(true);
        return new DefaultKafkaProducerFactory<>(producerConfigs(), new StringSerializer(), serializer);
    }

    @Bean
    public KafkaTemplate<String, User> userKafkaTemplate() {
        return new KafkaTemplate<>(userProducerFactory());
    }


    // Department
    @Bean
    public ProducerFactory<String, Department> departmentProducerFactory() {
        JsonSerializer<Department> serializer = new JsonSerializer<>();
        serializer.setUseTypeMapperForKey(true);
        return new DefaultKafkaProducerFactory<>(producerConfigs(), new StringSerializer(), serializer);
    }

    @Bean
    public KafkaTemplate<String, Department> departmentKafkaTemplate() {
        return new KafkaTemplate<>(departmentProducerFactory());
    }

    @Bean
    public KafkaProducer kafkaProducer() {
        return new KafkaProducer();
    }
}
