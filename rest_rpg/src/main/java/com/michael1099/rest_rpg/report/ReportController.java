package com.michael1099.rest_rpg.report;

import lombok.RequiredArgsConstructor;
import org.openapitools.api.ReportApi;
import org.openapitools.model.GenerateReportRequest;
import org.openapitools.model.ReportResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Validated
public class ReportController implements ReportApi {

    private final ReportService service;

    @Override
    public ResponseEntity<ReportResponse> generateReport(GenerateReportRequest generateReportRequest) {
        return ResponseEntity.ok(service.generateReport(generateReportRequest));
    }
}
