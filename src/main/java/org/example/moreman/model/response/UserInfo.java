package org.example.moreman.model.response;


import com.agro.public_.enums.Role;

public record UserInfo(
        Integer id,
        String email,
        String password,
        Role role
) {
}
