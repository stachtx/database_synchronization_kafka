package com.database.integration.external.kafka;

import com.database.integration.core.model.Department;
import com.database.integration.core.model.Product;
import com.database.integration.core.model.ProductType;
import com.database.integration.core.model.User;
import com.database.integration.external.kafka.producer.KafkaProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Deque;

@Component
public class KafkaCacheScheduler {

    @Autowired
    KafkaProducer kafkaProducer;

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaProducer.class);

    @Scheduled(cron = "${kafka.cache.cron}")
    public void sendCachedMessage() {
        Deque<Object> cachedMessages = KafkaMessageCache.getCachedMessages();
        for (Object message : cachedMessages) {
            KafkaMessageCache.remove(message);
            switch (message.getClass().getSimpleName()) {
                case "User":
                    kafkaProducer.send((User) message);
                    LOGGER.warn("Cached message User : " + message + " is sent");
                    break;
                case "Product":
                    kafkaProducer.send((Product) message);
                    LOGGER.warn("Cached message Product : " + message + " is sent");
                    break;
                case "ProductType":
                    kafkaProducer.send((ProductType) message);
                    LOGGER.warn("Cached message ProductType : " + message + " is sent");
                    break;
                case "Department":
                    kafkaProducer.send((Department) message);
                    LOGGER.warn("Cached message Department : " + message + " is sent");
                    break;
                default:
                    LOGGER.warn("Incorrect object type");
            }
        }
    }
}
