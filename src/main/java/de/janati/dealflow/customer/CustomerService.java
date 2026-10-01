package de.janati.dealflow.customer;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional
public class CustomerService {

    private final CustomerRepository repository;

    public CustomerService(CustomerRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<CustomerResponse> findAll() {
        return repository.findAll().stream().map(CustomerResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public CustomerResponse findById(Long id) {
        return CustomerResponse.from(getOrThrow(id));
    }

    public CustomerResponse create(CustomerRequest request) {
        if (request.email() != null && repository.existsByEmail(request.email())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already exists");
        }
        Customer customer = new Customer();
        apply(customer, request);
        return CustomerResponse.from(repository.save(customer));
    }

    public CustomerResponse update(Long id, CustomerRequest request) {
        Customer customer = getOrThrow(id);
        apply(customer, request);
        return CustomerResponse.from(customer);
    }

    public void delete(Long id) {
        repository.delete(getOrThrow(id));
    }

    private Customer getOrThrow(Long id) {
        return repository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found"));
    }

    private void apply(Customer customer, CustomerRequest request) {
        customer.setName(request.name());
        customer.setCompany(request.company());
        customer.setEmail(request.email());
    }
}