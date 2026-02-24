package com.example.demo.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Product;

@Repository
public interface ProductRepo extends JpaRepository<Product, Integer> {

    @Query("SELECT p from Product p WHERE " + "LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword,'%')) OR "
            + "LOWER(p.description) LIKE LOWER(CONCAT('%', :keyword,'%')) OR "
            + "LOWER(p.brand) LIKE LOWER(CONCAT('%', :keyword,'%') ) OR "
            + "LOWER(p.category) LIKE LOWER(CONCAT('%', :keyword,'%'))")

    List<Product> searchProducts(@Param("keyword") String keyword);

    @Query("SELECT p FROM Product p WHERE p.name = :name")
    List<Product> findByName(@Param("name") String name);

    @Query("SELECT p FROM Product p WHERE p.price > :price AND p.category = :cat")
    List<Product> findExpensive(@Param("price") Double price, @Param("cat") String cat);

    @Query(value = "SELECT * FROM Product WHERE price > :price", nativeQuery = true)
    List<Product> findByPrice(Double price);

    Page<Product> findByNameContaining(String name, Pageable pageable);

}
