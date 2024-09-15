package com.example.freeandnice.dto;

import lombok.Data;

import java.util.List;

@Data
public class ProductResponseDto {
    private Long id;
    private String name;
    private String description;
    private Long price;
    private Integer quantity;
    private String location;
    private List<String> imagePaths;
    private List<String> categoryNames;
}
