// FILE: (NOUVEAU) C:\Users\romeo\Documents\Projets\PhytoMarker-DB\phytomarker-db-back\src\main\java\fr\cda\phytomarker_db\controller\MarkerController.java
package fr.cda.phytomarker_db.controller;

import fr.cda.phytomarker_db.repository.MarkerRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/markers")
public class MarkerController {

    private final MarkerRepository markerRepository;

    public MarkerController(MarkerRepository markerRepository) {
        this.markerRepository = markerRepository;
    }

    @GetMapping
    public List<String> getAllMarkerNames() {
        // On retourne directement la liste des noms pour alléger la réponse.
        return markerRepository.findAll().stream()
                .map(marker -> marker.getName())
                .sorted() // Trier par ordre alphabétique pour un meilleur affichage
                .collect(Collectors.toList());
    }
}