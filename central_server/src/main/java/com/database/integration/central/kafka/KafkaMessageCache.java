package com.database.integration.central.kafka;

import static java.text.MessageFormat.format;

import java.util.ArrayDeque;
import java.util.Deque;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class KafkaMessageCache {

  private static final Deque<Object> cachedMessages = new ArrayDeque<>();


  public static Deque<Object> getCachedMessages() {
    return cachedMessages;
  }

  public static void put(Object message) {
    cachedMessages.push(message);
    log.info(format("Message with {0} : {1} is cached",
        message.getClass().getSimpleName(),
        message));
  }

  public static boolean remove(Object message) {
    return cachedMessages.remove(message);
  }
}
