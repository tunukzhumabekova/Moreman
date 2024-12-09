package com.ORT.model.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record UserRequest(
        String image,
        @NotBlank(message = "Full name cannot be empty")
        String name,
        @Min(value = 15, message = "Age must be at least 15")
        @Max(value = 70, message = "Age must be at most 70")
        Integer age,
        Integer schoolId,
        String phoneNumber
) {
}