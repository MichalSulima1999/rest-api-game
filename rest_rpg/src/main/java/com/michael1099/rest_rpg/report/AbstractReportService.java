package com.michael1099.rest_rpg.report;

import com.michael1099.rest_rpg.character.CharacterRepository;
import com.michael1099.rest_rpg.equipment.EquipmentRepository;
import com.michael1099.rest_rpg.statistics.StatisticsRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class AbstractReportService implements ReportService {

    protected final CharacterRepository characterRepository;
    protected final EquipmentRepository equipmentRepository;
    protected final StatisticsRepository statisticsRepository;
    protected final DefaultReportGenerator defaultReportGenerator;
    protected final JsonReportGenerator jsonReportGenerator;
    protected final YamlReportGenerator yamlReportGenerator;
}
