package com.ivan.javaguru.store_product_catalog.persistence.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Table(name = "product", schema = "public")
@Entity
public class Product {

    @Schema(description = "уникальный идентификатор товара")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long productId;

    @Schema(description = "название товара")
    private String name;

    @Schema(description = "цена товара")
    private BigDecimal price;

    @Schema(description = "категория товара")
    private String category;
}
