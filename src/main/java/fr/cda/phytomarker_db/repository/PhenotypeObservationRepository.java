package fr.cda.phytomarker_db.repository;

import fr.cda.phytomarker_db.model.PhenotypeObservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhenotypeObservationRepository extends JpaRepository<PhenotypeObservation, Long> {
}
