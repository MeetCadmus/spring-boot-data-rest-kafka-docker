package com.andreiev.maksym.tiara.TiaraChallenge;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
public class ProductRestTest {
    @Container
    static KafkaContainer kafkaContainer = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:latest"));

    @DynamicPropertySource
    static void kafkaProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.kafka.bootstrap-servers", kafkaContainer::getBootstrapServers);
    }

    @Value(value = "${local.server.port}")
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Value(value = "${spring.security.user.name}")
    private String userName;

    @Value(value = "${spring.security.user.password}")
    private String password;

    @Test
    public void shouldReturnProduct() throws Exception {
        assertThat(this.restTemplate.withBasicAuth(userName, password).getForObject("http://localhost:" + port + "/products/1", String.class)).contains("Test product 1");
    }

    @Test
    public void shouldReturnSecondPageProduct() throws Exception {
        assertThat(this.restTemplate.withBasicAuth(userName, password).getForObject("http://localhost:" + port + "/products?page=1&size=10", String.class)).contains("Test product 12");
    }

    @Test
    public void shouldReturnProductSortDescById() throws Exception {
        assertThat(this.restTemplate.withBasicAuth(userName, password).getForObject("http://localhost:" + port + "/products?page=0&size=10&sort=id,desc", String.class)).contains("Test product 29");
    }

    @Test
    public void shouldSearchProductByName() throws Exception {
        assertThat(this.restTemplate.withBasicAuth(userName, password).getForObject("http://localhost:" + port + "/products/search/findByName?name=Test product 2", String.class)).contains("Test product 2");
    }

    @Test
    public void shouldSearchProductByPrice() throws Exception {
        assertThat(this.restTemplate.withBasicAuth(userName, password).getForObject("http://localhost:" + port + "/products/search/findByPrice?price=1001", String.class)).contains("Test product 1");
    }

    @Test
    public void shouldCreateProduct() throws Exception {
        Product product = new Product("test", "test", 777);
        ResponseEntity<String> response = this.restTemplate.withBasicAuth(userName, password).postForEntity("http://localhost:" + port + "/products", product, String.class);
        assertEquals(201, response.getStatusCodeValue());
        String body = response.getBody();
        assertTrue(body != null && body.contains("test"));
    }

    @Test
    public void shouldCreateManyProducts() throws Exception {
        List<Product> products = Arrays.asList(new Product("Test123", "Desc", 1d), new Product("Test321", "Desc2", 2d));
        ResponseEntity<String> response = this.restTemplate.withBasicAuth(userName, password).postForEntity("http://localhost:" + port + "/products/batch", products, String.class);

        assertEquals(201, response.getStatusCodeValue());
        String body = response.getBody();
        assertTrue(body != null && body.contains("Test123") && body.contains("Test321"));
    }
}
