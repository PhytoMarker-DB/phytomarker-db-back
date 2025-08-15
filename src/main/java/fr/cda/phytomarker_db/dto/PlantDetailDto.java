package fr.cda.phytomarker_db.dto;

import java.util.List;
import java.util.Set;

public record PlantDetailDto(
        Long id,
        String name,
        String variety,
        Long parent1Id,
        Long parent2Id,
        Set<GenotypeDto> genotypes,
        List<PhenotypeObservationDto> observations
) {
}
