package com.example.freeandnice.services.impl;

import com.example.freeandnice.dto.CreateProductRequestDto;
import com.example.freeandnice.dto.ProductResponseDto;
import com.example.freeandnice.dto.UpdateProductRequestDto;
import com.example.freeandnice.models.Category;
import com.example.freeandnice.models.Product;
import com.example.freeandnice.models.User;
import com.example.freeandnice.repositories.CategoryRepository;
import com.example.freeandnice.repositories.ProductRepository;
import com.example.freeandnice.repositories.UserRepository;
import com.example.freeandnice.services.ProductService;
import io.jsonwebtoken.impl.security.EdwardsCurve;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    @Override
    public ProductResponseDto createProduct(CreateProductRequestDto createProductRequestDto) {
        Product product = new Product();
        product.setName(createProductRequestDto.getName());
        product.setDescription(createProductRequestDto.getDescription());
        product.setPrice(createProductRequestDto.getPrice());
        product.setQuantity(createProductRequestDto.getQuantity());
        product.setLocation(createProductRequestDto.getLocation());

        List<Category> categories = categoryRepository.findAllById(createProductRequestDto.getCategoryIds());
        product.setCategories(new HashSet<>(categories));

        Product savedProduct = productRepository.save(product);
        return mapToProductResponseDto(savedProduct);
    }

    @Override
    public Optional<ProductResponseDto> getProductById(Long id) {
        return productRepository.findById(id).map(this::mapToProductResponseDto);
    }

    @Override
    public List<ProductResponseDto> getAllProducts() {
        return productRepository.findAll()
                .stream()
                .map(this::mapToProductResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public ProductResponseDto updateProduct(UpdateProductRequestDto updateProductRequestDto) {
        Product product = productRepository.findById(updateProductRequestDto.getId())
                .orElseThrow(() -> new IllegalArgumentException("Product not found with id: " + updateProductRequestDto.getId()));

        product.setName(updateProductRequestDto.getName());
        product.setDescription(updateProductRequestDto.getDescription());
        product.setPrice(updateProductRequestDto.getPrice());
        product.setQuantity(updateProductRequestDto.getQuantity());
        product.setLocation(updateProductRequestDto.getLocation());

        List<Category> categories = categoryRepository.findAllById(updateProductRequestDto.getCategoryIds());
        product.setCategories(new HashSet<>(categories));

        Product updatedProduct = productRepository.save(product);
        return mapToProductResponseDto(updatedProduct);
    }

    @Override
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    @Override
    public List<ProductResponseDto> getProductsBySeller(Long sellerId) {
        User seller = userRepository.findById(sellerId)
                .orElseThrow(() -> new IllegalArgumentException("Seller not found with id: " + sellerId));
        List<Product> products = productRepository.findBySeller(seller);

        return products.stream()
                .map(this::mapToProductResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductResponseDto> searchByNameOrCategory(String keyword) {
        List<Product> products = productRepository.searchByName(keyword);
        return products.stream()
                .map(this::mapToProductResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductResponseDto> filterProducts(String category, Long minPrice, Long maxPrice) {
        List<Product> products = productRepository.filterProducts(category, minPrice, maxPrice);
        return products.stream()
                .map(this::mapToProductResponseDto)
                .collect(Collectors.toList());
    }

    private ProductResponseDto mapToProductResponseDto(Product product) {
        ProductResponseDto dto = new ProductResponseDto();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice());
        dto.setQuantity(product.getQuantity());
        dto.setLocation(product.getLocation());

        dto.setCategoryNames(product.getCategories().stream()
                .map(Category::getName)
                .collect(Collectors.toList()));

        dto.setImagePaths(new ArrayList<>(product.getImages()));

        return dto;
    }
}
