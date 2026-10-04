package de.janati.dealflow.customer;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    boolean existsByEmail(String email);
    List<Customer> findByNameContainingIgnoreCaseOrCompanyContainingIgnoreCase(String name, String company);
}