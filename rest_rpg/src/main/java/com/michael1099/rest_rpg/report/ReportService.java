package com.michael1099.rest_rpg.report;

import com.michael1099.rest_rpg.character.CharacterRepository;
import com.michael1099.rest_rpg.equipment.EquipmentRepository;
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
    private final DefaultReportGenerator reportGenerator;
    private final JsonReportGenerator jsonReportGenerator;
    private final YamlReportGenerator yamlReportGenerator;

    public ReportResponse generateReport(GenerateReportRequest generateReportRequest) {
        switch (generateReportRequest.getReportType()) {
            case CHARACTER -> {
                return generateCharacterReport(generateReportRequest.getId());
            }
            case EQUIPMENT -> {
                return generateEquipmentReport(generateReportRequest.getId());
            }
            case STATISTICS -> {
                return generateStatisticsReport(generateReportRequest.getId());
            }
            default -> throw new IllegalStateException("Unexpected value: " + generateReportRequest.getReportType());
        }
    }

    private ReportResponse generateCharacterReport(long characterId) {
        var character = characterRepository.getCharacterById(characterId);
        return character.accept(reportGenerator);
    }

    private ReportResponse generateEquipmentReport(long equipmentId) {
        var equipment = equipmentRepository.getEquipmentById(equipmentId);
        return equipment.accept(reportGenerator);
    }

    private ReportResponse generateStatisticsReport(long statisticsId) {
        var statistics = statisticsRepository.getStatisticsById(statisticsId);
        return statistics.accept(reportGenerator);
    }
}
