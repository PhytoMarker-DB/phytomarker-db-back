// FILE: C:\Users\romeo\Documents\Projets\PhytoMarker-DB\phytomarker-db-back\src\main\java\fr\cda\phytomarker_db\repository\PlantRepository.java
package fr.cda.phytomarker_db.repository;

import fr.cda.phytomarker_db.model.Plant;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PlantRepository extends JpaRepository<Plant, Long>, JpaSpecificationExecutor<Plant> {

    @Query("SELECT DISTINCT p.variety FROM Plant p ORDER BY p.variety ASC")
    List<String> findDistinctVarieties();

    // CORRECTION : Appliquer l'Entity Graph à la méthode de recherche.
    // Cela force l'initialisation des collections paresseuses DANS la même requête.
    @Override
    @EntityGraph(value = "Plant.withGenotypesAndMarkers")
    List<Plant> findAll(Specification<Plant> spec);

    // Il est aussi bon de l'appliquer à la recherche par ID pour la page de détail.
    @Override
    @EntityGraph(value = "Plant.withGenotypesAndMarkers")
    Optional<Plant> findById(Long id);
}