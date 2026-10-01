package de.janati.dealflow.customer;

public record CustomerResponse(Long id, String name, String company, String email) {
    static CustomerResponse from(Customer c) {
        return new CustomerResponse(c.getId(), c.getName(), c.getCompany(), c.getEmail());
    }
}