package com.michael1099.rest_rpg.report;

import org.openapitools.model.ReportFormat;

import java.util.HashMap;
import java.util.Map;

public class ReportGeneratorFactory {

    private static final Map<ReportFormat, ReportVisitor> generators = new HashMap<>();

    static {
        generators.put(ReportFormat.DEFAULT, new DefaultReportGenerator());
        generators.put(ReportFormat.YAML, new YamlReportGenerator());
        generators.put(ReportFormat.JSON, new JsonReportGenerator());
    }

    public static ReportVisitor getGenerator(ReportFormat format) {
        return generators.get(format);
    }
}
