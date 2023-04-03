package com.database.integration.external.kafka.producer;

import com.database.integration.core.model.Department;
import com.database.integration.core.model.products.Product;
import com.database.integration.core.model.products.ProductType;
import com.database.integration.core.model.users.User;
import com.database.integration.external.kafka.KafkaMessageCache;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.scheduling.annotation.Async;

import java.util.concurrent.CompletableFuture;

import static java.text.MessageFormat.format;

@Slf4j
public class KafkaProducer {

    @Autowired
    private KafkaTemplate<String, User> userTemplate;

    @Autowired
    private KafkaTemplate<String, Product> productTemplate;

    @Autowired
    private KafkaTemplate<String, ProductType> productTypeKafkaTemplate;

    @Autowired
    private KafkaTemplate<String, Department> departmentKafkaTemplate;


    private final String centralTopicUser = "central-topic-user";
    private final String centralTopicProduct = "central-topic-product";
    private final String centralTopicProductType = "central-topic-product-type";
    private final String centralTopicDepartment = "central-topic-department";

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaProducer.class);

    @Async
    public void send(User user) {
        handleResult(userTemplate.send(centralTopicUser, user), user);
    }

    @Async
    public void send(Product product) {
        handleResult(productTemplate.send(centralTopicProduct, product), product);
    }

    @Async
    public void send(ProductType productType) {
        handleResult(productTypeKafkaTemplate.send(centralTopicProductType, productType), productType);
    }

    @Async
    public void send(Department department) {
        handleResult(departmentKafkaTemplate.send(centralTopicDepartment, department), department);
    }


    private <T> void handleResult(CompletableFuture<SendResult<String, T>> future, T message) {

        future.whenComplete((metadata, exception) -> {
            if (exception == null) {
                log.info(format("Message [ {0} : {1} ] sent successfully to partition {2} with offset= [ {3} ]",
                        message.getClass().getSimpleName(), message, metadata.getRecordMetadata().partition(),
                        metadata.getRecordMetadata().offset()));

            } else {
                KafkaMessageCache.put(message);
                log.warn(format("Failed to send message =[ {0} : {1} ] due to : {2}",
                        message.getClass().getSimpleName(), message, exception.getMessage()));
            }
        });
    }
}
