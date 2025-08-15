package fr.cda.phytomarker_db.repository;

import fr.cda.phytomarker_db.model.Genotype;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GenotypeRepository extends JpaRepository<Genotype, Long> {}