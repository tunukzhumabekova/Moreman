package org.example.moreman.model.request;

public record ProductRecord(
        String name,
        String image,
        int price,
        int quantity,
        String description,
        boolean isPopular,
        int categoryId
) {
}
