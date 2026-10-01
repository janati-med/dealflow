package de.janati.dealflow.customer;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CustomerRequest(
        @NotBlank String name,
        String company,
        @Email String email) {
}