package gov.va.health.api.sentinel;

import static gov.va.health.api.sentinel.ResourceRequest.assertRequest;

import gov.va.api.health.argonaut.api.resources.OperationOutcome;
import gov.va.api.health.argonaut.api.resources.Practitioner;
import java.util.Arrays;
import java.util.List;
import org.junit.runners.Parameterized.Parameters;

public class PractitionerIT {

  @Parameters(name = "{index}: {0} {2}")
  public static List<Object[]> parameters() {
    ResourceRequest resourceRequest = new ResourceRequest();
    TestIds ids = IdRegistrar.of(Sentinel.get().system()).registeredIds();
    return Arrays.asList(
        assertRequest(200, Practitioner.class, "/api/Practitioner/{id}", ids.practitioner()),
        assertRequest(404, OperationOutcome.class, "/api/Practitioner/{id}", ids.unknown()),
        assertRequest(
            200, Practitioner.Bundle.class, "/api/Practitioner?_id={id}", ids.practitioner()),
        assertRequest(
            200,
            Practitioner.Bundle.class,
            "/api/Practitioner?identifier={id}",
            ids.practitioner()),
        assertRequest(404, OperationOutcome.class, "/api/Practitioner?_id={id}", ids.unknown()));
  }
}
