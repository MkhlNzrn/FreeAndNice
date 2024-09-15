package com.example.freeandnice.repositories;

import com.example.freeandnice.models.Product;
import com.example.freeandnice.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query("SELECT p FROM Product p " +
            "WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Product> searchByName(@Param("keyword") String keyword);

    @Query("SELECT p FROM Product p" +
            " JOIN p.categories c " +
            "WHERE (:category IS NULL OR c.name = :category)" +
            " AND (:minPrice IS NULL OR p.price >= :minPrice)" +
            " AND (:maxPrice IS NULL OR p.price <= :maxPrice)")
    List<Product> filterProducts(@Param("category") String category,
                                 @Param("minPrice") Long minPrice,
                                 @Param("maxPrice") Long maxPrice);

    List<Product> findBySeller(User seller);

}
