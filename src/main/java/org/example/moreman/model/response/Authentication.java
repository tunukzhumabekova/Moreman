package org.example.moreman.model.response;

import com.agro.public_.enums.Role;
import lombok.Builder;

@Builder
public record Authentication(
        int id,
        String email,
        String token,
        Role role) {}
