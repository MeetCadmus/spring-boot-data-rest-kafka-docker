package com.andreiev.maksym.tiara.TiaraChallenge;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.IntStream;

@Component
public class DatabaseLoader implements CommandLineRunner {
    private final ProductRepository productRepository;

    @Autowired
    public DatabaseLoader(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Value("${spring.kafka.bootstrap-servers}")
    private String kafka_host;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Kafka host - " + kafka_host);

        List<Product> products = IntStream.range(1, 30).
                mapToObj(i -> new Product("Test product " + i, "Test desc " + i, 1000L + i)).toList();
        this.productRepository.saveAll(products);
    }
}
