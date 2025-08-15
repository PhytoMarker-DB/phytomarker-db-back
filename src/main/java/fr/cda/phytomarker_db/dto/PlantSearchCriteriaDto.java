package fr.cda.phytomarker_db.dto;

import java.util.List;

public record PlantSearchCriteriaDto (
        String variety,
        Double minMildewScore,
        List<String> markerNames
) {

}
