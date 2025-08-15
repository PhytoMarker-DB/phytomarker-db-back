package fr.cda.phytomarker_db.repository;

import fr.cda.phytomarker_db.model.Marker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MarkerRepository extends JpaRepository<Marker, Long> {}