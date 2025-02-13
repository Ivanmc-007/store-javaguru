package com.ivan.javaguru.store_order.usecasses.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderCreateDto {

    @NotNull
    @Schema(description = "идентификатор пользователя, оформившего заказ")
    private Long userId;

    @NotNull
    @Schema(description = "идентификатор товара")
    private Long productId;

    @Min(value = 1)
    @Schema(description = "количество товара")
    private Integer quantity;
}
