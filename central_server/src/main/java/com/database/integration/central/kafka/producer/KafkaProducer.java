package com.database.integration.central.kafka.producer;


import static java.text.MessageFormat.format;

import com.database.integration.central.kafka.KafkaMessageCache;
import com.database.integration.core.model.Department;
import com.database.integration.core.model.products.Product;
import com.database.integration.core.model.products.ProductType;
import com.database.integration.core.model.users.User;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.scheduling.annotation.Async;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.concurrent.CompletableFuture;

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

  private final String externalTopicUser = "external-topic-user";
  private final String externalTopicProduct = "external-topic-product";
  private final String externalTopicProductType = "external-topic-product-type";
  private final String externalTopicDepartment = "external-topic-department";

  private static final Logger LOGGER = LoggerFactory.getLogger(KafkaProducer.class);

  @Async
  public void send(User user) {
    handleResult(userTemplate.send(externalTopicUser, user), user);
  }

  @Async
  public void send(Product product) {
    handleResult(productTemplate.send(externalTopicProduct, product), product);
  }

  @Async
  public void send(ProductType productType) {
    handleResult(productTypeKafkaTemplate.send(externalTopicProductType, productType), productType);
  }

  @Async
  public void send(Department department) {
    handleResult(departmentKafkaTemplate.send(externalTopicDepartment, department), department);
  }

  private <T> void handleResult(CompletableFuture<SendResult<String, T>> future, T message) {

    future.whenComplete((metadata, exception) -> {
      if (exception == null) {
        log.info(format("Message [ {0} : {1} ] sent successfully to partition {2} with offset= [ {3} ]",
                message.getClass().getSimpleName(), message,metadata.getRecordMetadata().partition(),
                metadata.getRecordMetadata().offset()));

      } else {
        KafkaMessageCache.put(message);
        log.warn(format("Failed to send message =[ {0} : {1} ] due to : {2}",
                message.getClass().getSimpleName(), message, exception.getMessage()));
      }
    });
  }
}
