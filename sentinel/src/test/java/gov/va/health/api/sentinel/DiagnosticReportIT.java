package gov.va.health.api.sentinel;

import static gov.va.health.api.sentinel.ResourceRequest.assertRequest;

import gov.va.api.health.argonaut.api.resources.DiagnosticReport;
import gov.va.api.health.argonaut.api.resources.OperationOutcome;
import java.util.Arrays;
import java.util.List;
import org.junit.runners.Parameterized.Parameters;

public class DiagnosticReportIT {

  @Parameters(name = "{index}: {0} {2}")
  public static List<Object[]> parameters() {
    ResourceRequest resourceRequest = new ResourceRequest();
    TestIds ids = IdRegistrar.of(Sentinel.get().system()).registeredIds();
    return Arrays.asList(
        assertRequest(
            200, DiagnosticReport.class, "/api/DiagnosticReport/{id}", ids.diagnosticReport()),
        assertRequest(404, OperationOutcome.class, "/api/DiagnosticReport/{id}", ids.unknown()),
        assertRequest(
            200,
            DiagnosticReport.Bundle.class,
            "api/DiagnosticReport?_id={id}",
            ids.diagnosticReport()),
        assertRequest(404, OperationOutcome.class, "api/DiagnosticReport?_id={id}", ids.unknown()),
        assertRequest(
            200,
            DiagnosticReport.Bundle.class,
            "api/DiagnosticReport?identifier={id}",
            ids.diagnosticReport()),
        assertRequest(
            404, OperationOutcome.class, "/api/DiagnosticReport?identifier={id}", ids.unknown()),
        assertRequest(
            200,
            DiagnosticReport.Bundle.class,
            "/api/DiagnosticReport?patient={patient}",
            ids.patient()));
  }
}
