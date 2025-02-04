package gov.va.api.health.dataquery.service.controller.diagnosticreport;

import gov.va.api.health.argonaut.api.resources.DiagnosticReport;
import gov.va.api.health.dataquery.service.controller.ExtractIcnValidator;
import gov.va.api.health.dstu2.api.elements.Reference;
import java.util.List;
import org.junit.Test;

public class Dstu2DiagnosticReportIncludesIcnMajigTest {

  @Test
  public void extractIcn() {
    ExtractIcnValidator.<Dstu2DiagnosticReportIncludesIcnMajig, DiagnosticReport>builder()
        .majig(new Dstu2DiagnosticReportIncludesIcnMajig())
        .body(
            DiagnosticReport.builder()
                .id("123")
                .subject(Reference.builder().reference("Patient/1010101010V666666").build())
                .build())
        .expectedIcns(List.of("1010101010V666666"))
        .build()
        .assertIcn();
  }
}
