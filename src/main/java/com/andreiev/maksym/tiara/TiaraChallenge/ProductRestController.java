package com.andreiev.maksym.tiara.TiaraChallenge;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.BasePathAwareController;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.stream.StreamSupport;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.http.HttpStatus.CREATED;

@BasePathAwareController
public class ProductRestController {
    private final ProductRepository productRepository;

    @Autowired
    public ProductRestController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @PostMapping("/products/batch")
    public ResponseEntity<?> createProducts(@RequestBody List<Product> products) {
        Iterable<Product> savedAll = productRepository.saveAll(products);

        List<EntityModel<Product>> result = StreamSupport.stream(savedAll.spliterator(), false)
                .map(p -> EntityModel.of(p, linkTo(ProductRepository.class).slash("/products/").slash(p.getId()).withSelfRel()))
                .toList();

        CollectionModel<EntityModel<Product>> resources = CollectionModel.of(result, linkTo(methodOn(ProductRestController.class).createProducts(null)).withSelfRel());
        return ResponseEntity.status(CREATED).body(resources);
    }
}
