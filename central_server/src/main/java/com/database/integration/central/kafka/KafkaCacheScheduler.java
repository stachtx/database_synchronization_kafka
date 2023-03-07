package com.database.integration.central.kafka;

import static java.text.MessageFormat.format;

import com.database.integration.central.kafka.producer.KafkaProducer;
import com.database.integration.core.model.Department;
import com.database.integration.core.model.products.Product;
import com.database.integration.core.model.products.ProductType;
import com.database.integration.core.model.users.User;
import java.util.Deque;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class KafkaCacheScheduler {

  @Autowired
  KafkaProducer kafkaProducer;

  @Scheduled(cron = "${kafka.cache.cron}")
  public void sendCachedMessage() {
    Deque<Object> cachedMessages = KafkaMessageCache.getCachedMessages();
    for (Object message : cachedMessages) {
      KafkaMessageCache.remove(message);
      switch (message.getClass().getSimpleName()) {
        case "User":
          kafkaProducer.send((User) message);
          log.info(format("Cached message User : {0} is sent",
              message));
          break;
        case "Product":
          kafkaProducer.send((Product) message);
          log.info(format("Cached message Product : {0} is sent",
              message));
          break;
        case "ProductType":
          kafkaProducer.send((ProductType) message);
          log.info(format("Cached message ProductType : {0} is sent",
              message));
          break;
        case "Department":
          kafkaProducer.send((Department) message);
          log.info(format("Cached message Department : {0} is sent",
              message));
          break;
        default:
          log.warn("Incorrect object type");
      }
    }
  }
}
