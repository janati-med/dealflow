package de.janati.dealflow.deal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.janati.dealflow.customer.Customer;
import de.janati.dealflow.customer.CustomerRepository;
import java.math.BigDecimal;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@ExtendWith(MockitoExtension.class)
class DealServiceTest {

    @Mock
    DealRepository deals;

    @Mock
    CustomerRepository customers;

    @InjectMocks
    DealService service;

    @Test
    void createSavesDealForExistingCustomer() {
        when(customers.findById(1L)).thenReturn(Optional.of(new Customer()));
        when(deals.save(any(Deal.class))).thenAnswer(inv -> inv.getArgument(0));

        DealResponse result = service.create(
                new DealRequest("Lizenzvertrag", new BigDecimal("25000"), 1L, "janati"));

        assertThat(result.title()).isEqualTo("Lizenzvertrag");
        assertThat(result.stage()).isEqualTo(DealStage.LEAD);
        verify(deals).save(any(Deal.class));
    }

    @Test
    void createFailsWhenCustomerDoesNotExist() {
        when(customers.findById(99L)).thenReturn(Optional.empty());

        assertThatExceptionOfType(ResponseStatusException.class)
                .isThrownBy(() -> service.create(
                        new DealRequest("Deal", BigDecimal.TEN, 99L, null)))
                .satisfies(ex -> assertThat(ex.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND));
    }

    @Test
    void moveToStageChangesStageOfOpenDeal() {
        Deal deal = dealWithStage(DealStage.LEAD);
        when(deals.findById(1L)).thenReturn(Optional.of(deal));

        DealResponse result = service.moveToStage(1L, DealStage.ANGEBOT);

        assertThat(result.stage()).isEqualTo(DealStage.ANGEBOT);
    }

    @Test
    void moveToStageRejectsClosedDeal() {
        Deal deal = dealWithStage(DealStage.GEWONNEN);
        when(deals.findById(1L)).thenReturn(Optional.of(deal));

        assertThatExceptionOfType(ResponseStatusException.class)
                .isThrownBy(() -> service.moveToStage(1L, DealStage.LEAD))
                .satisfies(ex -> assertThat(ex.getStatusCode()).isEqualTo(HttpStatus.CONFLICT));
        assertThat(deal.getStage()).isEqualTo(DealStage.GEWONNEN);
    }

    @Test
    void moveToStageFailsForUnknownDeal() {
        when(deals.findById(42L)).thenReturn(Optional.empty());

        assertThatExceptionOfType(ResponseStatusException.class)
                .isThrownBy(() -> service.moveToStage(42L, DealStage.ANGEBOT))
                .satisfies(ex -> assertThat(ex.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND));
    }

    private Deal dealWithStage(DealStage stage) {
        Deal deal = new Deal();
        deal.setTitle("Test");
        deal.setCustomer(new Customer());
        deal.setStage(stage);
        return deal;
    }
}