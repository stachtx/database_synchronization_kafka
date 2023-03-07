package com.database.integration.external.kafka.consumer;

import com.database.integration.core.model.users.User;
import com.database.integration.external.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;

public class KafkaReceiver {

    @Autowired
    private UserService userService;

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaReceiver.class);

    @KafkaListener(id = "central-user-receiver", topics = "central-topic-user", containerFactory = "userKafkaListenerContainerFactory")
    public void receive(User user) {
        userService.mergeUser(user);
        LOGGER.info("Received user: " + user.toString());
    }
}
