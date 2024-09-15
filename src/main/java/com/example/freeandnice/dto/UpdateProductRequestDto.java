package com.example.freeandnice.dto;

import lombok.Data;

import java.util.List;

@Data
public class UpdateProductRequestDto {
    private Long id;
    private String name;
    private String description;
    private Long price;
    private Integer quantity;
    private String location;
    private List<String> imagePaths;
    private List<Long> categoryIds;
}
