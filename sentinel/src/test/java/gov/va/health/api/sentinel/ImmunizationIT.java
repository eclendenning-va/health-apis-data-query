package gov.va.health.api.sentinel;

import static gov.va.health.api.sentinel.ResourceRequest.assertRequest;

import gov.va.api.health.argonaut.api.resources.Immunization;
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
public class ImmunizationIT {

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
        assertRequest(200, Immunization.class, "/api/Immunization/{id}", ids.immunization()),
        assertRequest(404, OperationOutcome.class, "/api/Immunization/{id}", ids.unknown()),
        assertRequest(
            200, Immunization.Bundle.class, "/api/Immunization?_id={id}", ids.immunization()),
        assertRequest(
            200,
            Immunization.Bundle.class,
            "/api/Immunization?identifier={id}",
            ids.immunization()),
        assertRequest(404, OperationOutcome.class, "/api/Immunization?_id={id}", ids.unknown()),
        assertRequest(
            200, Immunization.Bundle.class, "/api/Immunization?patient={patient}", ids.patient()));
  }

  @Test
  public void resourceRequestTest() {
    resourceRequest.getResource(path, params, status, response);
    resourceRequest.pagingParameterBounds(path, params, response);
  }
}
