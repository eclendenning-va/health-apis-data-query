package gov.va.health.api.sentinel;

import static gov.va.health.api.sentinel.ResourceRequest.assertRequest;

import gov.va.api.health.argonaut.api.resources.Location;
import gov.va.api.health.argonaut.api.resources.OperationOutcome;
import java.util.Arrays;
import java.util.List;
import org.junit.runners.Parameterized.Parameters;

public class LocationIT {

  @Parameters(name = "{index}: {0} {2}")
  public static List<Object[]> parameters() {
    ResourceRequest resourceRequest = new ResourceRequest();
    TestIds ids = IdRegistrar.of(Sentinel.get().system()).registeredIds();
    return Arrays.asList(
        assertRequest(200, Location.class, "/api/Location/{id}", ids.location()),
        assertRequest(404, OperationOutcome.class, "/api/Location/{id}", ids.unknown()),
        assertRequest(200, Location.Bundle.class, "/api/Location?_id={id}", ids.location()),
        assertRequest(200, Location.Bundle.class, "/api/Location?identifier={id}", ids.location()),
        assertRequest(404, OperationOutcome.class, "/api/Location?_id={id}", ids.unknown()));
  }
}
