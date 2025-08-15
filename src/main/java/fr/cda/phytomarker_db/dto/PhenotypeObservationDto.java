package fr.cda.phytomarker_db.dto;

import java.time.LocalDate;

public record PhenotypeObservationDto(LocalDate observationDate, String trait, String value) {}


