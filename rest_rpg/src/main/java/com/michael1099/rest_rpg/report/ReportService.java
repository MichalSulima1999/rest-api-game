package com.michael1099.rest_rpg.report;

import org.openapitools.model.GenerateReportRequest;
import org.openapitools.model.ReportResponse;

public interface ReportService {

    ReportResponse generateReport(GenerateReportRequest generateReportRequest);

    ReportResponse generateReportDataControl(GenerateReportRequest generateReportRequest);
}
