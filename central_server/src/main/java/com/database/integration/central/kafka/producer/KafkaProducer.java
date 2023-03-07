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

  private String externalTopicUser = "external-topic-user";
  private String externalTopicProduct = "external-topic-product";
  private String externalTopicProductType = "external-topic-product-type";
  private String externalTopicDepartment = "external-topic-department";

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

  private <T> void handleResult(ListenableFuture<SendResult<String, T>> future, T message) {
    future.addCallback(new ListenableFutureCallback<SendResult<String, T>>() {
      @Override
      public void onSuccess(SendResult<String, T> result) {
        log.info(format("Sent message = [ {0} : {1} ] with offset=[ {2} ]",
            message.getClass().getSimpleName(), message,
            result.getRecordMetadata().offset()));
      }

      @Override
      public void onFailure(Throwable ex) {
        KafkaMessageCache.put(message);
        log.warn(format("Unable to send message =[ {0} : {1} ] due to : {2}",
            message.getClass().getSimpleName(), message, ex.getMessage()));
      }
    });
  }
}
