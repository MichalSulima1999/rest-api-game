package com.michael1099.rest_rpg.report;

import com.michael1099.rest_rpg.character.model.Character;
import com.michael1099.rest_rpg.equipment.Equipment;
import com.michael1099.rest_rpg.statistics.Statistics;
import org.openapitools.model.ReportResponse;

// Tydzień 6 - visitor
// Stworzony został interfejs ReportVisitor, który posiada definicję przeciążonej metody visit
// Metoda może przyjąć Character, Statistics, Equipment. Interfejs jest zaimplementowany w ReportGenerator, gdzie znajduje się logika.
// Interfejs Reportable ma metodę accept, która przyjmuje visitora. Ten interfejs jest implementowany przez Character, Statistics, Equipment.
// W tych klasach wywoływana jest metoda visit z obiektem, z którego jest wywoływana.
// W tym wypadku visitor pozwala na generowanie raportów poszczególnych obiektów.
public interface ReportVisitor {

    ReportResponse visit(Character character);

    ReportResponse visit(Statistics statistics);

    ReportResponse visit(Equipment equipment);
}
// Koniec Tydzień 6 - visitor
