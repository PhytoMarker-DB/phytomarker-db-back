// FILE: C:\Users\romeo\Documents\Projets\PhytoMarker-DB\phytomarker-db-back\src\main\java\fr\cda\phytomarker_db\model\Plant.java
package fr.cda.phytomarker_db.model;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

// CORRECTION : Ajout de la définition de l'Entity Graph
@NamedEntityGraph(
        name = "Plant.withGenotypesAndMarkers",
        attributeNodes = {
                @NamedAttributeNode(value = "genotypes", subgraph = "genotype-subgraph")
        },
        subgraphs = {
                @NamedSubgraph(
                        name = "genotype-subgraph",
                        attributeNodes = {
                                @NamedAttributeNode("marker")
                        }
                )
        }
)
@Entity
@Table(name = "plants")
public class Plant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String variety;

    private Double mildewResistanceScore;

    private Long parent1Id;
    private Long parent2Id;

    // Le FetchType.LAZY est correct et doit être conservé.
    // L'Entity Graph le surchargera uniquement pour les requêtes où il est appliqué.
    @OneToMany(mappedBy = "plant", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<Genotype> genotypes = new HashSet<>();

    @OneToMany(mappedBy = "plant", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<PhenotypeObservation> phenotypeObservations = new HashSet<>();

    // Getters et Setters restent inchangés...
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVariety() {
        return variety;
    }

    public void setVariety(String variety) {
        this.variety = variety;
    }

    public Double getMildewResistanceScore() {
        return mildewResistanceScore;
    }

    public void setMildewResistanceScore(Double mildewResistanceScore) {
        this.mildewResistanceScore = mildewResistanceScore;
    }

    public Long getParent1Id() {
        return parent1Id;
    }

    public void setParent1Id(Long parent1Id) {
        this.parent1Id = parent1Id;
    }

    public Long getParent2Id() {
        return parent2Id;
    }

    public void setParent2Id(Long parent2Id) {
        this.parent2Id = parent2Id;
    }

    public Set<Genotype> getGenotypes() {
        return genotypes;
    }

    public void setGenotypes(Set<Genotype> genotypes) {
        this.genotypes = genotypes;
    }

    public Set<PhenotypeObservation> getPhenotypeObservations() {
        return phenotypeObservations;
    }

    public void setPhenotypeObservations(Set<PhenotypeObservation> phenotypeObservations) {
        this.phenotypeObservations = phenotypeObservations.isEmpty() ? new HashSet<>() : phenotypeObservations;
    }
}