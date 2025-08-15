// FILE: C:\Users\romeo\Documents\Projets\PhytoMarker-DB\phytomarker-db-back\src\main\java\fr\cda\phytomarker_db\service\PlantService.java
package fr.cda.phytomarker_db.service;

import fr.cda.phytomarker_db.dto.*;
import fr.cda.phytomarker_db.exception.ResourceNotFoundException;
import fr.cda.phytomarker_db.model.Plant;
import fr.cda.phytomarker_db.repository.PlantRepository;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class PlantService {
    private final PlantRepository plantRepository;

    public PlantService(PlantRepository plantRepository) {
        this.plantRepository = plantRepository;
    }   

    // ... (searchPlants, createPlant, etc. restent les mêmes)
    public List<PlantDto> searchPlants(PlantSearchCriteriaDto criteria) {
        Specification<Plant> spec = Specification.where(null);

        if (criteria.variety() != null && !criteria.variety().isBlank()) {
            spec = spec.and(PlantSpecification.hasVariety(criteria.variety()));
        }

        if (criteria.minMildewScore() != null) {
            spec = spec.and(PlantSpecification.hasMinMildewScore(criteria.minMildewScore()));
        }

        if (criteria.markerNames() != null && !criteria.markerNames().isEmpty()) {
            spec = spec.and(PlantSpecification.hasMarkers(criteria.markerNames()));
        }

        return plantRepository.findAll(spec).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PlantDetailDto getPlantById(Long id) {
        Plant plant = plantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Plant not found with id: " + id));
        return convertToDetailDto(plant);
    }

    @Transactional
    public Plant createPlant(Plant newPlant) {
        return plantRepository.save(newPlant);
    }

    /**
     * Récupère l'ascendance complète d'une plante sous forme de liste de nœuds de pedigree.
     * Utilise une approche récursive pour remonter l'arbre généalogique.
     */
    @Transactional(readOnly = true)
    public List<PedigreeNodeDto> getPedigreeForPlant(Long startPlantId) {
        Set<PedigreeNodeDto> nodes = new HashSet<>();
        Set<Long> processedIds = new HashSet<>();
        buildPedigreeRecursive(startPlantId, nodes, processedIds);
        return new ArrayList<>(nodes);
    }

    private void buildPedigreeRecursive(Long plantId, Set<PedigreeNodeDto> nodes, Set<Long> processedIds) {
        if (plantId == null || processedIds.contains(plantId)) {
            return;
        }

        processedIds.add(plantId);

        Optional<Plant> plantOpt = plantRepository.findById(plantId);
        if (plantOpt.isEmpty()) {
            // Si un parent n'existe pas dans la DB, on le crée comme un nœud "fantôme"
            nodes.add(new PedigreeNodeDto(String.valueOf(plantId), "Parent inconnu " + plantId, Collections.emptyList(), false));
            return;
        }

        Plant plant = plantOpt.get();
        List<String> parentIds = new ArrayList<>();
        if (plant.getParent1Id() != null) parentIds.add(String.valueOf(plant.getParent1Id()));
        if (plant.getParent2Id() != null) parentIds.add(String.valueOf(plant.getParent2Id()));

        // On vérifie si la plante a des marqueurs d'intérêt, par exemple.
        boolean hasMarkerOfInterest = !plant.getGenotypes().isEmpty();

        nodes.add(new PedigreeNodeDto(String.valueOf(plant.getId()), plant.getName(), parentIds, hasMarkerOfInterest));

        if (plant.getParent1Id() != null) {
            buildPedigreeRecursive(plant.getParent1Id(), nodes, processedIds);
        }
        if (plant.getParent2Id() != null) {
            buildPedigreeRecursive(plant.getParent2Id(), nodes, processedIds);
        }
    }


    public String exportToCsv(PlantSearchCriteriaDto criteria) {
        List<PlantDto> plants = searchPlants(criteria);
        StringBuilder csvBuilder = new StringBuilder();
        csvBuilder.append("ID,Nom,Variete,Score Mildiou,Marqueurs\n");
        for (PlantDto plant : plants) {
            String markers = plant.genotypes().stream()
                    .map(g -> g.marker().name())
                    .collect(Collectors.joining("|")); // Utiliser un séparateur différent de la virgule
            csvBuilder.append(String.format("%d,\"%s\",\"%s\",%.2f,\"%s\"\n",
                    plant.id(), plant.name(), plant.variety(), plant.mildewResistanceScore(), markers));
        }
        return csvBuilder.toString();
    }

    // ... (les méthodes de conversion DTO restent les mêmes)
    private PlantDto convertToDto(Plant plant) {
        Set<GenotypeDto> genotypeDtos = plant.getGenotypes() != null ?
                plant.getGenotypes().stream()
                        .map(genotype -> new GenotypeDto(new MarkerDto(genotype.getMarker().getName())))
                        .collect(Collectors.toSet())
                : Set.of();

        return new PlantDto(
                plant.getId(),
                plant.getName(),
                plant.getVariety(),
                plant.getMildewResistanceScore(),
                genotypeDtos
        );
    }

    private PlantDetailDto convertToDetailDto(Plant plant) {
        Set<GenotypeDto> genotypeDtos = plant.getGenotypes() != null ?
                plant.getGenotypes().stream()
                        .map(genotype -> new GenotypeDto(new MarkerDto(genotype.getMarker().getName())))
                        .collect(Collectors.toSet())
                : Set.of();

        List<PhenotypeObservationDto> observationDtos = plant.getPhenotypeObservations() != null ?
                plant.getPhenotypeObservations().stream()
                        .map(obs -> new PhenotypeObservationDto(obs.getObservationDate(), obs.getTrait(), obs.getValue()))
                        .collect(Collectors.toList())
                : List.of();

        return new PlantDetailDto(
                plant.getId(),
                plant.getName(),
                plant.getVariety(),
                plant.getParent1Id(),
                plant.getParent2Id(),
                genotypeDtos,
                observationDtos
        );
    }
}