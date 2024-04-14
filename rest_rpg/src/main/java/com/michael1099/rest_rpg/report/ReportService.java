package com.michael1099.rest_rpg.report;

import com.michael1099.rest_rpg.character.CharacterRepository;
import com.michael1099.rest_rpg.equipment.EquipmentRepository;
import com.michael1099.rest_rpg.exceptions.EnumValueNotFoundException;
import com.michael1099.rest_rpg.statistics.StatisticsRepository;
import lombok.RequiredArgsConstructor;
import org.openapitools.model.GenerateReportRequest;
import org.openapitools.model.ReportResponse;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@RequiredArgsConstructor
@Validated
public class ReportService {

    private final CharacterRepository characterRepository;
    private final EquipmentRepository equipmentRepository;
    private final StatisticsRepository statisticsRepository;
    private final DefaultReportGenerator defaultReportGenerator;
    private final JsonReportGenerator jsonReportGenerator;
    private final YamlReportGenerator yamlReportGenerator;

    public ReportResponse generateReport(GenerateReportRequest generateReportRequest) {
        ReportVisitor visitor;
        switch (generateReportRequest.getReportFormat()) {
            case DEFAULT -> visitor = new DefaultReportGenerator();
            case YAML -> visitor = new YamlReportGenerator();
            case JSON -> visitor = new JsonReportGenerator();
            default -> throw new EnumValueNotFoundException();
        }

        switch (generateReportRequest.getReportType()) {
            case CHARACTER -> {
                return generateCharacterReport(generateReportRequest.getId(), visitor);
            }
            case EQUIPMENT -> {
                return generateEquipmentReport(generateReportRequest.getId(), visitor);
            }
            case STATISTICS -> {
                return generateStatisticsReport(generateReportRequest.getId(), visitor);
            }
            default -> throw new IllegalStateException("Unexpected value: " + generateReportRequest.getReportType());
        }
    }

    public ReportResponse generateReportDataControl(GenerateReportRequest generateReportRequest) {
        return switch (generateReportRequest.getReportFormat()) {
            case DEFAULT -> generate(generateReportRequest, defaultReportGenerator);
            case YAML -> generate(generateReportRequest, yamlReportGenerator);
            case JSON -> generate(generateReportRequest, jsonReportGenerator);
        };
    }

    private ReportResponse generate(GenerateReportRequest generateReportRequest, ReportVisitor generator) {
        switch (generateReportRequest.getReportType()) {
            case CHARACTER -> {
                return generateCharacterReport(generateReportRequest.getId(), generator);
            }
            case EQUIPMENT -> {
                return generateEquipmentReport(generateReportRequest.getId(), generator);
            }
            case STATISTICS -> {
                return generateStatisticsReport(generateReportRequest.getId(), generator);
            }
            default -> throw new IllegalStateException("Unexpected value: " + generateReportRequest.getReportType());
        }
    }

    private ReportResponse generateCharacterReport(long characterId, ReportVisitor generator) {
        var character = characterRepository.getCharacterById(characterId);
        return character.accept(generator);
    }

    private ReportResponse generateEquipmentReport(long equipmentId, ReportVisitor generator) {
        var equipment = equipmentRepository.getEquipmentById(equipmentId);
        return equipment.accept(generator);
    }

    private ReportResponse generateStatisticsReport(long statisticsId, ReportVisitor generator) {
        var statistics = statisticsRepository.getStatisticsById(statisticsId);
        return statistics.accept(generator);
    }
}
