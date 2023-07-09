package com.andreiev.maksym.tiara.TiaraChallenge;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.annotation.HandleAfterCreate;
import org.springframework.data.rest.core.annotation.HandleAfterDelete;
import org.springframework.data.rest.core.annotation.HandleAfterSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RepositoryEventHandler
public class ProductRestEventListener {
    private final Logger logger = LoggerFactory.getLogger(ProductRestEventListener.class);
    private KafkaTemplate<String, String> template;
    private final String TOPIC_NAME = "topic1";


    @HandleAfterCreate
    public void handleProductAfterCreate(Product product) {
        logger.info("Sending message to kafka that product created");
        template.send(TOPIC_NAME, "Created  " + product);
    }

    @HandleAfterSave
    public void handleProductAfterSave(Product product) {
        logger.info("Sending message to kafka that product saved");
        template.send(TOPIC_NAME, "Saved  " + product);
    }

    @HandleAfterDelete
    public void handleProductAfterDelete(Product product) {
        logger.info("Sending message to kafka that product deleted");
        template.send(TOPIC_NAME, "Deleted  " + product);
    }

    @Autowired
    public void setTemplate(KafkaTemplate<String, String> template) {
        this.template = template;
    }
}
