package de.janati.dealflow.deal;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DealRepository extends JpaRepository<Deal, Long> {
    List<Deal> findByStage(DealStage stage);
}