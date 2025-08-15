// FILE: C:\Users\romeo\Documents\Projets\PhytoMarker-DB\phytomarker-db-back\src\main\java\fr\cda\phytomarker_db\dto\PedigreeNodeDto.java
package fr.cda.phytomarker_db.dto;

import java.util.List;

/**
 * DTO représentant un nœud dans un arbre généalogique (pedigree).
 * Conçu pour être directement utilisable par une librairie de visualisation comme D3.js.
 */
public record PedigreeNodeDto(
        String id,
        String name,
        List<String> parents,
        boolean hasMarker // Exemple de métadonnée pour la coloration
) {}