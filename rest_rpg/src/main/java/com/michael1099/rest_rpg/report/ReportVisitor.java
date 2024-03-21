package com.michael1099.rest_rpg.report;

import com.michael1099.rest_rpg.character.model.Character;
import com.michael1099.rest_rpg.equipment.Equipment;
import com.michael1099.rest_rpg.statistics.Statistics;
import org.openapitools.model.ReportResponse;

public interface ReportVisitor {

    ReportResponse visit(Character character);

    ReportResponse visit(Statistics statistics);

    ReportResponse visit(Equipment equipment);
}
