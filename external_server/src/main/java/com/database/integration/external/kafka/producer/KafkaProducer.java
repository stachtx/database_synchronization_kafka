package com.database.integration.external.kafka.producer;

import com.database.integration.external.kafka.KafkaMessageCache;
import com.database.integration.core.model.Department;
import com.database.integration.core.model.products.Product;
import com.database.integration.core.model.products.ProductType;
import com.database.integration.core.model.users.User;
import java.text.MessageFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.scheduling.annotation.Async;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

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


    private <T> void handleResult(ListenableFuture<SendResult<String, T>> future, T message) {
        future.addCallback(new ListenableFutureCallback<SendResult<String, T>>() {
            @Override
            public void onSuccess(SendResult<String, T> result) {
                LOGGER.info(MessageFormat.format("Sent message = [ {0} : {1} ] with offset=[ {2} ]",
                    message.getClass().getSimpleName(), message,
                    result.getRecordMetadata().offset()));
            }

            @Override
            public void onFailure(Throwable ex) {
                KafkaMessageCache.put(message);
                LOGGER.warn(MessageFormat.format("Unable to send message =[ {0} : {1} ] due to : {2}",
                        message.getClass().getSimpleName(), message, ex.getMessage()));
            }
        });
    }
}
