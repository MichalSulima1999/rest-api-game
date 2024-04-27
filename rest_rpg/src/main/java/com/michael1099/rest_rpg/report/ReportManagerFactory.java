package com.michael1099.rest_rpg.report;

import com.michael1099.rest_rpg.character.model.Character;
import com.michael1099.rest_rpg.equipment.Equipment;
import com.michael1099.rest_rpg.statistics.Statistics;

public class ReportManagerFactory {

    public static ReportManager getCharacterManager(Character character) {
        return new CharacterReportManagerImplementation(character);
    }

    public static ReportManager getStatisticsManager(Statistics statistics) {
        return new StatisticsReportManagerImplementation(statistics);
    }

    public static ReportManager getEquipmentManager(Equipment equipment) {
        return new EquipmentReportManagerImplementation(equipment);
    }
}
