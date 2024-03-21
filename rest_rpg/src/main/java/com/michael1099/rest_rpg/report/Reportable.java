package com.michael1099.rest_rpg.report;

import org.openapitools.model.ReportResponse;

public interface Reportable {

    ReportResponse accept(ReportVisitor visitor);
}
