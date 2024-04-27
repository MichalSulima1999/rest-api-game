package com.michael1099.rest_rpg.report;

import org.openapitools.model.ReportResponse;

public interface ReportManager {

    ReportResponse generateDefaultReport();

    ReportResponse generateJsonReport();

    ReportResponse generateYamlReport();
}
