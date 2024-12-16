package org.example.moreman.model.response;

import lombok.Builder;

@Builder
public record Authentication(
        int id,
        String email,
        String token) {}
