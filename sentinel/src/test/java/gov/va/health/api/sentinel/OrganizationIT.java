package gov.va.health.api.sentinel;

import static gov.va.health.api.sentinel.ResourceRequest.assertRequest;

import gov.va.api.health.argonaut.api.resources.OperationOutcome;
import gov.va.api.health.argonaut.api.resources.Organization;
import java.util.Arrays;
import java.util.List;
import org.junit.runners.Parameterized.Parameters;

public class OrganizationIT {

  @Parameters(name = "{index}: {0} {2}")
  public static List<Object[]> parameters() {
    ResourceRequest resourceRequest = new ResourceRequest();
    TestIds ids = IdRegistrar.of(Sentinel.get().system()).registeredIds();
    return Arrays.asList(
        assertRequest(200, Organization.class, "/api/Organization/{id}", ids.organization()),
        assertRequest(404, OperationOutcome.class, "/api/Organization/{id}", ids.unknown()),
        assertRequest(
            200, Organization.Bundle.class, "/api/Organization?_id={id}", ids.organization()),
        assertRequest(
            200,
            Organization.Bundle.class,
            "/api/Organization?identifier={id}",
            ids.organization()),
        assertRequest(404, OperationOutcome.class, "/api/Organization?_id={id}", ids.unknown()));
  }
}
