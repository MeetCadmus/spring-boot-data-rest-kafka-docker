package com.andreiev.maksym.tiara.TiaraChallenge;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(path = "products", exported = true )
public interface ProductRepository extends PagingAndSortingRepository<Product, Long> {
    List<Product> findByName(@Param("name") String name);

    List<Product> findByPrice(@Param("price") Double price);
}
