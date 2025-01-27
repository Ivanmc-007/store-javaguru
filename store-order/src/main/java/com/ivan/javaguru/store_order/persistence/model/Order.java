package com.ivan.javaguru.store_order.persistence.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "t_order", schema = "public")
public class Order {

    @Schema(description = "уникальный идентификатор заказа")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long orderId;

    @Schema(description = "идентификатор пользователя, оформившего заказ")
    @Column(name = "user_id")
    private Long userId;

    @Schema(description = "идентификатор товара")
    @Column(name = "product_id")
    private Long productId;

    @Schema(description = "количество товара")
    private Integer quantity;
}
