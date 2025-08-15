// FILE: C:\Users\romeo\Documents\Projets\PhytoMarker-DB\phytomarker-db-back\src\main\java\fr\cda\phytomarker_db\init\DataInitializer.java
package fr.cda.phytomarker_db.init;

import fr.cda.phytomarker_db.model.Genotype;
import fr.cda.phytomarker_db.model.Marker;
import fr.cda.phytomarker_db.model.PhenotypeObservation;
import fr.cda.phytomarker_db.model.Plant;
import fr.cda.phytomarker_db.repository.GenotypeRepository;
import fr.cda.phytomarker_db.repository.MarkerRepository;
import fr.cda.phytomarker_db.repository.PhenotypeObservationRepository;
import fr.cda.phytomarker_db.repository.PlantRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Component
@Profile("dev") // Ne s'exécute que lorsque le profil 'dev' est actif
public class DataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    private final PlantRepository plantRepository;
    private final MarkerRepository markerRepository;
    private final GenotypeRepository genotypeRepository;
    private final PhenotypeObservationRepository phenotypeObservationRepository; // Ajout du repository

    public DataInitializer(PlantRepository plantRepository, MarkerRepository markerRepository,
                           GenotypeRepository genotypeRepository, PhenotypeObservationRepository phenotypeObservationRepository) {
        this.plantRepository = plantRepository;
        this.markerRepository = markerRepository;
        this.genotypeRepository = genotypeRepository;
        this.phenotypeObservationRepository = phenotypeObservationRepository; // Injection de la dépendance
    }

    @Override
    @Transactional // Envelopper dans une transaction pour assurer la cohérence
    public void run(String... args) throws Exception {
        if (plantRepository.count() > 0) {
            log.info("La base de données PhytoMarker contient déjà des données. L'initialisation est ignorée.");
            return;
        }

        log.info("############################################################");
        log.info("####### DÉMARRAGE DE L'INITIALISATION PHYTO-MARKER #######");

        // 1. Créer les marqueurs génétiques
        Marker mkR123 = new Marker();
        mkR123.setName("Mk-R-123");

        Marker mkR125 = new Marker();
        mkR125.setName("Mk-R-125");

        Marker mkY45 = new Marker();
        mkY45.setName("Mk-Y-45");

        markerRepository.saveAll(List.of(mkR123, mkR125, mkY45));
        log.info("{} marqueurs créés.", markerRepository.count());

        // 2. Créer les plantes et leur pedigree
        Plant grandParent = new Plant();
        grandParent.setName("Ancetre-01");
        grandParent.setVariety("Variété Ancienne");
        grandParent.setMildewResistanceScore(2.0);
        plantRepository.save(grandParent); // Sauvegarder pour obtenir un ID

        Plant parentA = new Plant();
        parentA.setName("Parent-A");
        parentA.setVariety("Blé dur");
        parentA.setMildewResistanceScore(3.5);
        parentA.setParent1Id(grandParent.getId());
        plantRepository.save(parentA);

        Plant parentB = new Plant();
        parentB.setName("Parent-B");
        parentB.setVariety("Orge");
        parentB.setMildewResistanceScore(4.0);
        // Ce parent n'a pas de parent connu dans notre DB
        plantRepository.save(parentB);

        Plant child1 = new Plant();
        child1.setName("Descendant-001");
        child1.setVariety("Blé dur");
        child1.setMildewResistanceScore(4.5);
        child1.setParent1Id(parentA.getId());
        child1.setParent2Id(parentB.getId());
        plantRepository.save(child1);

        Plant child2 = new Plant();
        child2.setName("Descendant-002");
        child2.setVariety("Blé dur");
        child2.setMildewResistanceScore(5.0);
        child2.setParent1Id(parentA.getId());
        child2.setParent2Id(parentB.getId());
        plantRepository.save(child2);

        Plant orphanPlant = new Plant();
        orphanPlant.setName("Variete-X");
        orphanPlant.setVariety("Soja");
        orphanPlant.setMildewResistanceScore(3.0);
        plantRepository.save(orphanPlant);


        log.info("{} plantes créées.", plantRepository.count());

        // 3. Créer les génotypes (lier les plantes aux marqueurs)
        createGenotype(grandParent, mkY45);
        createGenotype(parentA, mkY45);
        createGenotype(child1, mkR123);
        createGenotype(child1, mkR125);
        createGenotype(child2, mkR123);
        createGenotype(orphanPlant, mkR125);

        log.info("{} relations de génotype créées.", genotypeRepository.count());

        // 4. Créer les observations phénotypiques (NOUVELLE SECTION)
        createObservation(child1, "Hauteur (cm)", "110", LocalDate.of(2024, 7, 15));
        createObservation(child1, "Date Floraison", "2024-08-01", LocalDate.of(2024, 8, 1));
        createObservation(child1, "Résistance Mildiou", "Très résistant", LocalDate.of(2024, 8, 20));

        createObservation(child2, "Hauteur (cm)", "115", LocalDate.of(2024, 7, 16));
        createObservation(child2, "Résistance Mildiou", "Exceptionnelle", LocalDate.of(2024, 8, 21));

        createObservation(parentA, "Hauteur (cm)", "95", LocalDate.of(2023, 7, 10));

        log.info("{} observations phénotypiques créées.", phenotypeObservationRepository.count());

        log.info("############ INITIALISATION PHYTO-MARKER TERMINÉE ###########");
        log.info("############################################################");
    }

    /**
     * Méthode utilitaire pour créer et sauvegarder une relation Genotype.
     */
    private void createGenotype(Plant plant, Marker marker) {
        Genotype genotype = new Genotype();
        genotype.setPlant(plant);
        genotype.setMarker(marker);
        genotypeRepository.save(genotype);
    }

    /**
     * Méthode utilitaire pour créer et sauvegarder une observation phénotypique.
     */
    private void createObservation(Plant plant, String trait, String value, LocalDate date) {
        PhenotypeObservation observation = new PhenotypeObservation();
        observation.setPlant(plant);
        observation.setTrait(trait);
        observation.setValue(value);
        observation.setObservationDate(date);
        phenotypeObservationRepository.save(observation);
    }
}