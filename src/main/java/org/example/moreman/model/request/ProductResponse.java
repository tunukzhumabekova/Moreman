package org.example.moreman.model.request;

public record ProductResponse(
        int id,
        String name,
        String image,
        int price,
        int quantity,
        String description,
        boolean isPopular,
        int categoryId,
        int sale
) {
}
