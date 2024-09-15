package com.example.freeandnice.services;

import com.example.freeandnice.dto.CreateProductRequestDto;
import com.example.freeandnice.dto.ProductResponseDto;
import com.example.freeandnice.dto.UpdateProductRequestDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface ProductService {
    ProductResponseDto createProduct(CreateProductRequestDto createProductRequestDto);

    List<ProductResponseDto> searchByNameOrCategory(String keyword);

    List<ProductResponseDto> filterProducts(String category, Long minPrice, Long maxPrice);

    Optional<ProductResponseDto> getProductById(Long id);

    List<ProductResponseDto> getAllProducts();

    ProductResponseDto updateProduct(UpdateProductRequestDto updateProductRequestDto);

    void deleteProduct(Long id);

    List<ProductResponseDto> getProductsBySeller(Long sellerId);

}
