package de.janati.dealflow.customer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    CustomerRepository repository;

    @InjectMocks
    CustomerService service;

    @Test
    void createSavesCustomer() {
        when(repository.existsByEmail("max@beispiel.de")).thenReturn(false);
        when(repository.save(any(Customer.class))).thenAnswer(inv -> inv.getArgument(0));

        CustomerResponse result = service.create(
                new CustomerRequest("Max", "Beispiel GmbH", "max@beispiel.de"));

        assertThat(result.name()).isEqualTo("Max");
        verify(repository).save(any(Customer.class));
    }

    @Test
    void createRejectsDuplicateEmail() {
        when(repository.existsByEmail("max@beispiel.de")).thenReturn(true);

        assertThatThrownBy(() -> service.create(
                new CustomerRequest("Max", null, "max@beispiel.de")))
                .isInstanceOf(ResponseStatusException.class);
    }

    @Test
    void findByIdThrowsWhenMissing() {
        when(repository.findById(99L)).thenReturn(java.util.Optional.empty());

        assertThatThrownBy(() -> service.findById(99L))
                .isInstanceOf(ResponseStatusException.class);
    }
}