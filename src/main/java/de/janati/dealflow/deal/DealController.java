package de.janati.dealflow.deal;

import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/deals")
public class DealController {

    private final DealService service;

    public DealController(DealService service) {
        this.service = service;
    }

    @GetMapping
    public List<DealResponse> list(@RequestParam(required = false) DealStage stage) {
        return service.findAll(stage);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DealResponse create(@Valid @RequestBody DealRequest request) {
        return service.create(request);
    }

    @PatchMapping("/{id}/stage")
    public DealResponse moveToStage(@PathVariable Long id, @RequestParam DealStage stage) {
        return service.moveToStage(id, stage);
    }
}