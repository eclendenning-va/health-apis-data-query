package gov.va.health.api.sentinel;

import static gov.va.health.api.sentinel.ResourceRequest.assertRequest;

import gov.va.api.health.argonaut.api.resources.MedicationStatement;
import gov.va.api.health.argonaut.api.resources.OperationOutcome;
import java.util.Arrays;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

@SuppressWarnings({"DefaultAnnotationParam", "WeakerAccess"})
@RunWith(Parameterized.class)
@Slf4j
public class MedicationStatementIT {

  @Parameter(0)
  public int status;

  @Parameter(1)
  public Class<?> response;

  @Parameter(2)
  public String path;

  @Parameter(3)
  public String[] params;

  ResourceRequest resourceRequest = new ResourceRequest();

  @Parameters(name = "{index}: {0} {2}")
  public static List<Object[]> parameters() {
    TestIds ids = IdRegistrar.of(Sentinel.get().system()).registeredIds();
    return Arrays.asList(
        assertRequest(
            200,
            MedicationStatement.class,
            "/api/MedicationStatement/{id}",
            ids.medicationStatement()),
        assertRequest(404, OperationOutcome.class, "/api/MedicationStatement/{id}", ids.unknown()),
        assertRequest(
            200,
            MedicationStatement.Bundle.class,
            "/api/MedicationStatement?_id={id}",
            ids.medicationStatement()),
        assertRequest(
            200,
            MedicationStatement.Bundle.class,
            "/api/MedicationStatement?identifier={id}",
            ids.medicationStatement()),
        assertRequest(
            404, OperationOutcome.class, "/api/MedicationStatement?_id={id}", ids.unknown()),
        assertRequest(
            200,
            MedicationStatement.Bundle.class,
            "/api/MedicationStatement?patient={patient}",
            ids.patient()));
  }

  @Test
  public void resourceRequestTest() {
    resourceRequest.getResource(path, params, status, response);
    resourceRequest.pagingParameterBounds(path, params, response);
  }
}
