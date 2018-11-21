  kage gov.va.api.health.argonaut.service.controller.diagnosticreport;

import gov.va.api.health.argonaut.api.datatypes.CodeableConcept;
import gov.va.api.health.argonaut.api.datatypes.Coding;
import gov.va.api.health.argonaut.api.elements.Reference;
import gov.va.api.health.argonaut.api.resources.DiagnosticReport;
import gov.va.api.health.argonaut.service.controller.EnumSearcher;
import gov.va.api.health.argonaut.service.controller.Parameters;
import gov.va.dvp.cdw.xsd.model.*;
import gov.va.dvp.cdw.xsd.model.CdwDiagnosticReport102Root.CdwDiagnosticReports.CdwDiagnosticReport;
import org.springframework.stereotype.Service;

import javax.xml.datatype.XMLGregorianCalendar;
import java.util.List;

import static gov.va.api.health.argonaut.service.controller.Transformers.*;

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
        .subject(reference(source.getSubject()))
        .encounter(reference(source.getEncounter()))
        .effectiveDateTime(asDateString(source.getEffective()))
        // .effectivePeriod()
        .issued(asDateTimeString(source.getIssued()))
        .performer(reference(source.getPerformer()))
        .request(request(source.getRequests()))
        .specimen(specimen(source.getSpecimens()))
        .result(result(source.getResults()))
        .build();
  }

   List<Reference> result(CdwDiagnosticReport.CdwResults optionalSource) {
    return convertAll(optionalSource.getResult(), this::reference);
  }

   List<Reference> specimen(CdwDiagnosticReport.CdwSpecimens optionalSource) {
    return convertAll(optionalSource.getSpecimen(), this::reference);
  }

   List<Reference> request(CdwDiagnosticReport.CdwRequests optionalSource) {
    return convertAll(optionalSource.getRequest(), this::reference);
  }

   Reference reference(CdwReference source) {
    return Reference.builder()
        .reference(source.getReference())
        .display(source.getDisplay())
        .build();
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

   DiagnosticReport.Code status(CdwDiagnosticReport source) {
    EnumSearcher<DiagnosticReport.Code> e = EnumSearcher.of(DiagnosticReport.Code.class).build();
    return ifPresent(source.getStatus(), status -> e.find(status.value()));
  }
}
