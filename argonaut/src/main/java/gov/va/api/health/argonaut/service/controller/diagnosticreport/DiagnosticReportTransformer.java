package gov.va.api.health.argonaut.service.controller.diagnosticreport;

import gov.va.api.health.argonaut.api.resources.DiagnosticReport;
import gov.va.dvp.cdw.xsd.model.CdwDiagnosticReport102Root.CdwDiagnosticReports.CdwDiagnosticReport;
import org.springframework.stereotype.Service;

@Service
public class DiagnosticReportTransformer implements DiagnosticReportController.Transformer {

    @Override
    public DiagnosticReport apply(CdwDiagnosticReport source) {
        return diagnosticReport(source);
    }

    private DiagnosticReport diagnosticReport(CdwDiagnosticReport source) {
        return DiagnosticReport.builder()
                .id(source.getCdwId())
                .resourceType("Diagnostic Report")
                .build();
    }
}
