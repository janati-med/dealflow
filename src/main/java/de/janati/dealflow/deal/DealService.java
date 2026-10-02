package de.janati.dealflow.deal;

import de.janati.dealflow.customer.CustomerRepository;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional
public class DealService {

    private final DealRepository deals;
    private final CustomerRepository customers;

    public DealService(DealRepository deals, CustomerRepository customers) {
        this.deals = deals;
        this.customers = customers;
    }

    @Transactional(readOnly = true)
    public List<DealResponse> findAll(DealStage stage) {
        List<Deal> result = (stage == null) ? deals.findAll() : deals.findByStage(stage);
        return result.stream().map(DealResponse::from).toList();
    }

    public DealResponse create(DealRequest request) {
        Deal deal = new Deal();
        deal.setTitle(request.title());
        deal.setValue(request.value());
        deal.setOwner(request.owner());
        deal.setCustomer(customers.findById(request.customerId()).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found")));
        return DealResponse.from(deals.save(deal));
    }

    public DealResponse moveToStage(Long id, DealStage newStage) {
        Deal deal = deals.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Deal not found"));
        if (deal.getStage() == DealStage.GEWONNEN || deal.getStage() == DealStage.VERLOREN) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Deal is already closed");
        }
        deal.setStage(newStage);
        return DealResponse.from(deal);
    }
}