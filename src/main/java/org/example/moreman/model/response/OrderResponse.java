package org.example.moreman.model.response;

import java.time.LocalDateTime;

public record OrderResponse(
        Integer orderId,
        Integer productId,
        Integer quantity,
        LocalDateTime localDateTime
        )
{}
