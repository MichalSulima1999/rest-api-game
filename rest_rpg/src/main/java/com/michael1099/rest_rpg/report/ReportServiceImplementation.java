package com.michael1099.rest_rpg.report;

import com.michael1099.rest_rpg.character.CharacterRepository;
import com.michael1099.rest_rpg.equipment.EquipmentRepository;
import com.michael1099.rest_rpg.statistics.StatisticsRepository;
import org.openapitools.model.GenerateReportRequest;
import org.openapitools.model.ReportResponse;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
public class ReportServiceImplementation extends AbstractReportService {

    public ReportServiceImplementation(CharacterRepository characterRepository, EquipmentRepository equipmentRepository, StatisticsRepository statisticsRepository) {
        super(characterRepository, equipmentRepository, statisticsRepository);
    }

    @Override
    public ReportResponse generateReport(GenerateReportRequest generateReportRequest) {
        var visitor = ReportGeneratorFactory.getGenerator(generateReportRequest.getReportFormat());

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

    @Override
    public ReportResponse generateReportDataControl(GenerateReportRequest generateReportRequest) {
        var character = characterRepository.getCharacterById(generateReportRequest.getId());
        return ReportManagerFactory.getCharacterManager(character).generateDefaultReport();
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
