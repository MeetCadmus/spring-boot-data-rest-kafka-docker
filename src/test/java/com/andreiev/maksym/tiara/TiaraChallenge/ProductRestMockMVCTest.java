package com.andreiev.maksym.tiara.TiaraChallenge;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Testcontainers
@SpringBootTest
@AutoConfigureMockMvc
public class ProductRestMockMVCTest {
    @Container
    static KafkaContainer kafkaContainer = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:latest"));

    @DynamicPropertySource
    static void kafkaProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.kafka.bootstrap-servers", kafkaContainer::getBootstrapServers);
    }
    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldReturnUnAuthenticatedWhenGetProducts() throws Exception {
        this.mockMvc.perform(get("/products/1")).andDo(print()).andExpect(status().isUnauthorized());
    }

    @WithMockUser(value = "user")
    @Test
    public void shouldReturnProduct1() throws Exception {
        this.mockMvc.perform(get("/products/1")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("Test product 1")));
    }

    @WithMockUser(value = "user")
    @Test
    public void shouldCreateManyProducts() throws Exception {
        List<Product> products = Arrays.asList(new Product("Test 345", "Desc", 1d), new Product("Test 543", "Desc", 2d));

        this.mockMvc.perform(
                post("/products/batch")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(products))
        ).andDo(print()).andExpect(status().isCreated()).andExpect(content().string(containsString("Test 345")));
    }

    private String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
