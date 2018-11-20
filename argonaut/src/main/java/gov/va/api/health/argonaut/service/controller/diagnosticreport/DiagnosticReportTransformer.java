package gov.va.api.health.argonaut.service.controller.diagnosticreport;

import gov.va.api.health.argonaut.api.datatypes.CodeableConcept;
import gov.va.api.health.argonaut.api.datatypes.Coding;
import gov.va.api.health.argonaut.api.resources.DiagnosticReport;
import gov.va.api.health.argonaut.service.controller.Parameters;
import gov.va.dvp.cdw.xsd.model.*;
import gov.va.dvp.cdw.xsd.model.CdwDiagnosticReport102Root.CdwDiagnosticReports.CdwDiagnosticReport;
import org.springframework.stereotype.Service;

import java.util.List;

import static gov.va.api.health.argonaut.service.controller.Transformers.convertAll;
import static gov.va.api.health.argonaut.service.controller.Transformers.ifPresent;

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
                .status(status(source))
                .category(category(source.getCategory()))
                .code(code(source.getCode()))
                .build();
    }

    //TODO: Stopping here for now
    private CodeableConcept code(CdwDiagnosticReportCode source) {
        return null;
    }

    private CodeableConcept category(CdwDiagnosticReportCategory source) {
        return CodeableConcept.builder()
                .coding(categoryCodings(source.getCoding()))
                .text(source.getText())
                .build();
    }

    private List<Coding> categoryCodings(List<CdwDiagnosticReportCategoryCoding> optionalSource) {
        return convertAll(
                optionalSource,
                cdw -> Coding.builder()
                        .system(cdw.getSystem())
                        .code(ifPresent(cdw.getCode(), CdwDiagnosticReportCategoryCode::value))
                        .build());
    }

    private DiagnosticReport.Code status(CdwDiagnosticReport source) {
        return ifPresent(source.getStatus(), status -> DiagnosticReport.Code.valueOf(status.value()));
    }
}
