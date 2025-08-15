// FILE: C:\Users\romeo\Documents\Projets\PhytoMarker-DB\phytomarker-db-back\src\main\java\fr\cda\phytomarker_db\controller\PlantController.java
package fr.cda.phytomarker_db.controller;

import fr.cda.phytomarker_db.dto.PedigreeNodeDto;
import fr.cda.phytomarker_db.dto.PlantDetailDto;
import fr.cda.phytomarker_db.dto.PlantDto;
import fr.cda.phytomarker_db.dto.PlantSearchCriteriaDto;
import fr.cda.phytomarker_db.model.Plant;
import fr.cda.phytomarker_db.repository.PlantRepository;
import fr.cda.phytomarker_db.service.PlantService;
import jakarta.validation.Valid;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/plants")
public class PlantController {
    private final PlantService plantService;
    private final PlantRepository plantRepository;

    public PlantController(PlantService plantService, PlantRepository plantRepository) {
        this.plantService = plantService;
        this.plantRepository = plantRepository;
    }

    @GetMapping("/search")
    public List<PlantDto> searchPlants(
            @RequestParam(required = false) String variety,
            @RequestParam(required = false) Double minMildewScore,
            @RequestParam(required = false) List<String> markerNames
    ) {
        PlantSearchCriteriaDto criteria = new PlantSearchCriteriaDto(variety, minMildewScore, markerNames);
        return plantService.searchPlants(criteria);
    }

    @GetMapping("/search/export")
    public ResponseEntity<Resource> exportSearch(PlantSearchCriteriaDto criteria) {
        String csvData = plantService.exportToCsv(criteria);
        byte[] csvBytes = csvData.getBytes();
        Resource resource = new ByteArrayResource(csvBytes);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"export_plants.csv\"")
                .contentType(MediaType.parseMediaType("text/csv"))
                .contentLength(csvBytes.length)
                .body(resource);
    }

    @GetMapping("/{id}")
    public PlantDetailDto getPlantById(@PathVariable Long id) {
        return plantService.getPlantById(id);
    }

    @GetMapping("/{id}/pedigree")
    public List<PedigreeNodeDto> getPedigree(@PathVariable Long id) {
        return plantService.getPedigreeForPlant(id);
    }

    @PostMapping
    public Plant createPlant(@Valid @RequestBody Plant plant) {
        return plantService.createPlant(plant);
    }

    @GetMapping("/varieties")
    public List<String> getAllVarieties() {
        return plantRepository.findDistinctVarieties();
    }
}