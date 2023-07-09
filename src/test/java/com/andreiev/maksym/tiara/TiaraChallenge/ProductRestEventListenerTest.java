package com.andreiev.maksym.tiara.TiaraChallenge;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import static org.mockito.Mockito.atMostOnce;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ProductRestEventListenerTest {
    private static final String TOPIC_1 = "topic1";
    @Mock
    private KafkaTemplate<String, String> templateMock;
    @InjectMocks
    private ProductRestEventListener productRestEventListener;
    private final Product testProduct = new Product("test", "test", 1);

    @Test
    void shouldHandleProductAfterCreate() {
        productRestEventListener.handleProductAfterCreate(testProduct);
        verify(templateMock, atMostOnce()).send(TOPIC_1, "Created  " + testProduct.toString());
    }

    @Test
    void shouldHandleProductAfterSave() {
        productRestEventListener.handleProductAfterSave(testProduct);
        verify(templateMock, atMostOnce()).send(TOPIC_1, "Saved  " + testProduct.toString());
    }

    @Test
    void shouldHandleProductAfterDelete() {
        productRestEventListener.handleProductAfterSave(testProduct);
        verify(templateMock, atMostOnce()).send(TOPIC_1, "Deleted  " + testProduct.toString());
    }
}