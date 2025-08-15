package fr.cda.phytomarker_db.service;

import fr.cda.phytomarker_db.model.Genotype;
import fr.cda.phytomarker_db.model.Marker;
import fr.cda.phytomarker_db.model.Plant;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.List;

public class PlantSpecification {
    public static Specification<Plant> hasVariety(String variety) {
        return (root, query, builder) ->
                StringUtils.hasText(variety) ? builder.equal(root.get("variety"), variety) : builder.conjunction();
    }

    public static Specification<Plant> hasMinMildewScore(Double minScore) {
        return (root, query, builder) ->
                minScore != null ? builder.greaterThanOrEqualTo(root.get("mildewResistanceScore"), minScore) : builder.conjunction();
    }

    public static Specification<Plant> hasMarkers(List<String> markerNames) {
        return (root, query, builder) -> {
            if (markerNames == null || markerNames.isEmpty()) {
                return builder.conjunction();
            }
            // Jointure complexe pour trouver les plantes possédant un marqueur donné
            Join<Plant, Genotype> genotypeJoin = root.join("genotypes");
            Join<Genotype, Marker> markerJoin = genotypeJoin.join("marker");
            return markerJoin.get("name").in(markerNames);
        };
    }
}
