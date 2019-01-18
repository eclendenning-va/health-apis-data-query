package gov.va.health.api.sentinel;

import static gov.va.health.api.sentinel.ResourceRequest.assertRequest;

import gov.va.api.health.argonaut.api.resources.Condition;
import gov.va.api.health.argonaut.api.resources.OperationOutcome;
import java.util.Arrays;
import java.util.List;
import org.junit.runners.Parameterized.Parameters;

public class ConditionIT {

  @Parameters(name = "{index}: {0} {2}")
  public static List<Object[]> parameters() {
    ResourceRequest resourceRequest = new ResourceRequest();
    TestIds ids = IdRegistrar.of(Sentinel.get().system()).registeredIds();
    return Arrays.asList(
        assertRequest(200, Condition.class, "/api/Condition/{id}", ids.condition()),
        assertRequest(404, OperationOutcome.class, "/api/Condition/{id}", ids.unknown()),
        assertRequest(
            200, Condition.Bundle.class, "/api/Condition?identifier={id}", ids.condition()),
        assertRequest(404, OperationOutcome.class, "/api/Condition?_id={id}", ids.unknown()),
        assertRequest(
            200, Condition.Bundle.class, "/api/Condition?patient={patient}", ids.patient()));
  }
}
