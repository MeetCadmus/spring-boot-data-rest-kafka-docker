package com.andreiev.maksym.tiara.TiaraChallenge;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumer {
    private final Logger logger = LoggerFactory.getLogger(KafkaConsumer.class);

    @KafkaListener(id = "myId", topics = "topic1")
    public void listen(String in) {
        logger.info("Consumed : " + in);
    }
}
