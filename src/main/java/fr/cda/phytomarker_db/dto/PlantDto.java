package fr.cda.phytomarker_db.dto;

import java.util.Set;

public record PlantDto(
        Long id,
        String name,
        String variety,
        Double mildewResistanceScore,
        Set<GenotypeDto> genotypes
) {
}
