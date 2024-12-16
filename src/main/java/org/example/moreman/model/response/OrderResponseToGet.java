package org.example.moreman.model.response;

import java.time.LocalDateTime;

public record OrderResponseToGet(
        Integer orderId,
        String productName,
        Integer price,
        Integer quantity,
        String description,
        LocalDateTime localDateTime
)
{}
