package de.janati.dealflow.deal;

import java.math.BigDecimal;

public record DealResponse(Long id, String title, BigDecimal value,
                           DealStage stage, Long customerId, String owner) {
    static DealResponse from(Deal d) {
        return new DealResponse(d.getId(), d.getTitle(), d.getValue(),
                d.getStage(), d.getCustomer().getId(), d.getOwner());
    }
}