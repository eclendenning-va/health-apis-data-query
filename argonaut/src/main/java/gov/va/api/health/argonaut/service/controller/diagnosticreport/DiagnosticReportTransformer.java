package gov.va.api.health.argonaut.service.controller.diagnosticreport;

import static gov.va.api.health.argonaut.service.controller.Transformers.asDateString;
import static gov.va.api.health.argonaut.service.controller.Transformers.asDateTimeString;
import static gov.va.api.health.argonaut.service.controller.Transformers.convert;
import static gov.va.api.health.argonaut.service.controller.Transformers.convertAll;
import static gov.va.api.health.argonaut.service.controller.Transformers.ifPresent;

import gov.va.api.health.argonaut.api.datatypes.CodeableConcept;
import gov.va.api.health.argonaut.api.datatypes.Coding;
import gov.va.api.health.argonaut.api.elements.Reference;
import gov.va.api.health.argonaut.api.resources.DiagnosticReport;
import gov.va.api.health.argonaut.service.controller.EnumSearcher;
import gov.va.dvp.cdw.xsd.model.CdwDiagnosticReport102Root.CdwDiagnosticReports.CdwDiagnosticReport;
import gov.va.dvp.cdw.xsd.model.CdwDiagnosticReportCategory;
import gov.va.dvp.cdw.xsd.model.CdwDiagnosticReportCategoryCode;
import gov.va.dvp.cdw.xsd.model.CdwDiagnosticReportCategoryCoding;
import gov.va.dvp.cdw.xsd.model.CdwDiagnosticReportCategoryDisplay;
import gov.va.dvp.cdw.xsd.model.CdwDiagnosticReportCode;
import gov.va.dvp.cdw.xsd.model.CdwDiagnosticReportCodeCoding;
import gov.va.dvp.cdw.xsd.model.CdwReference;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class DiagnosticReportTransformer implements DiagnosticReportController.Transformer {

  @Override
  public DiagnosticReport apply(CdwDiagnosticReport source) {
    return diagnosticReport(source);
  }

  CodeableConcept category(CdwDiagnosticReportCategory source) {
    return CodeableConcept.builder()
        .coding(categoryCodings(source.getCoding()))
        .text(source.getText())
        .build();
  }

  List<Coding> categoryCodings(List<CdwDiagnosticReportCategoryCoding> optionalSource) {
    return convertAll(
        optionalSource,
        cdw ->
            Coding.builder()
                .system(cdw.getSystem())
                .code(ifPresent(cdw.getCode(), CdwDiagnosticReportCategoryCode::value))
                .display(ifPresent(cdw.getDisplay(), CdwDiagnosticReportCategoryDisplay::value))
                .build());
  }

  CodeableConcept code(CdwDiagnosticReportCode source) {
    return CodeableConcept.builder()
        .coding(codeCodings(source.getCoding()))
        .text(source.getText())
        .build();
  }

  List<Coding> codeCodings(List<CdwDiagnosticReportCodeCoding> source) {
    return convertAll(
        source, cdw -> Coding.builder().system(cdw.getSystem()).code(cdw.getCode()).build());
  }

  private DiagnosticReport diagnosticReport(CdwDiagnosticReport source) {
    return DiagnosticReport.builder()
        .id(source.getCdwId())
        .resourceType("Diagnostic Report")
        .status(status(source))
        .category(category(source.getCategory()))
        .code(code(source.getCode()))
        .subject(reference(source.getSubject()))
        .encounter(reference(source.getEncounter()))
        .effectiveDateTime(asDateString(source.getEffective()))
        .issued(asDateTimeString(source.getIssued()))
        .performer(reference(source.getPerformer()))
        .build();
  }

  Reference reference(CdwReference maybeSource) {
    return convert(
        maybeSource,
        source ->
            Reference.builder()
                .reference(source.getReference())
                .display(source.getDisplay())
                .build());
  }

  DiagnosticReport.Code status(CdwDiagnosticReport source) {
    EnumSearcher<DiagnosticReport.Code> e = EnumSearcher.of(DiagnosticReport.Code.class);
    return ifPresent(source.getStatus(), status -> e.find(status.value()));
  }
}
