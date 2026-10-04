package de.janati.dealflow.mcp;

import de.janati.dealflow.customer.CustomerResponse;
import de.janati.dealflow.customer.CustomerService;
import de.janati.dealflow.deal.DealResponse;
import de.janati.dealflow.deal.DealService;
import de.janati.dealflow.deal.DealStage;
import java.util.List;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
public class DealflowTools {

    private final CustomerService customers;
    private final DealService deals;

    public DealflowTools(CustomerService customers, DealService deals) {
        this.customers = customers;
        this.deals = deals;
    }

    @Tool(description = "Search customers by name or company. Case-insensitive, partial match.")
    public List<CustomerResponse> searchCustomers(
            @ToolParam(description = "Text to look for in the customer name or company") String query) {
        return customers.search(query);
    }

    @Tool(description = "List deals in the sales pipeline. Stages are LEAD, ANGEBOT, VERHANDLUNG, "
            + "GEWONNEN (won) and VERLOREN (lost). Leave the stage empty to list all deals.")
    public List<DealResponse> listDeals(
            @ToolParam(description = "Pipeline stage to filter by", required = false) DealStage stage) {
        return deals.findAll(stage);
    }

    @Tool(description = "Move an open deal to another pipeline stage. "
            + "Deals that are already GEWONNEN or VERLOREN are closed and cannot be moved.")
    public DealResponse moveDeal(
            @ToolParam(description = "Id of the deal") Long dealId,
            @ToolParam(description = "Target stage") DealStage stage) {
        try {
            return deals.moveToStage(dealId, stage);
        } catch (ResponseStatusException e) {
            // give the AI a readable reason instead of a raw HTTP status
            throw new IllegalStateException(e.getReason());
        }
    }
}