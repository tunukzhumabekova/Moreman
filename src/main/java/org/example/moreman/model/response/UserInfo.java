package com.ORT.model.response;

import com.databil.mentormind.public_.enums.Role;

public record UserInfo(
        Integer id,
        String email,
        String password,
        Role role
) {
}
