package com.ORT.model.response;

import com.databil.mentormind.public_.enums.Role;
import lombok.Builder;

@Builder
public record Authentication(
        int id,
        String email,
        String token,
        Role role,
        String userName
) {
}