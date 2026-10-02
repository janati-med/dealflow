package de.janati.dealflow.deal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;

public record DealRequest(
        @NotBlank String title,
        @NotNull @PositiveOrZero BigDecimal value,
        @NotNull Long customerId,
        String owner) {
}