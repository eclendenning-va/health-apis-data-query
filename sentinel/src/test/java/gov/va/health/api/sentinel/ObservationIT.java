package gov.va.health.api.sentinel;

import static gov.va.health.api.sentinel.ResourceRequest.assertRequest;

import gov.va.api.health.argonaut.api.resources.Observation;
import gov.va.api.health.argonaut.api.resources.OperationOutcome;
import java.util.Arrays;
import java.util.List;
import org.junit.runners.Parameterized.Parameters;

public class ObservationIT {

  @Parameters(name = "{index}: {0} {2}")
  public static List<Object[]> parameters() {
    ResourceRequest resourceRequest = new ResourceRequest();
    TestIds ids = IdRegistrar.of(Sentinel.get().system()).registeredIds();
    return Arrays.asList(
        assertRequest(200, Observation.class, "/api/Observation/{id}", ids.observation()),
        assertRequest(404, OperationOutcome.class, "/api/Observation/{id}", ids.unknown()),
        assertRequest(
            200, Observation.Bundle.class, "/api/Observation?_id={id}", ids.observation()),
        assertRequest(
            200, Observation.Bundle.class, "/api/Observation?identifier={id}", ids.observation()),
        assertRequest(404, OperationOutcome.class, "/api/Observation?_id={id}", ids.unknown()),
        assertRequest(
            200, Observation.Bundle.class, "/api/Observation?patient={patient}", ids.patient()));
  }
}
