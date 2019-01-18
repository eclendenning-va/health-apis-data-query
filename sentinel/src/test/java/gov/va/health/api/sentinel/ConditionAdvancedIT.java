package gov.va.health.api.sentinel;

import static gov.va.health.api.sentinel.ResourceRequest.assertRequest;

import gov.va.api.health.argonaut.api.resources.Condition;
import java.util.Arrays;
import java.util.List;
import org.junit.runners.Parameterized.Parameters;

public class ConditionAdvancedIT {

  @Parameters(name = "{index}: {0} {2}")
  public static List<Object[]> parameters() {
    ResourceRequest resourceRequest = new ResourceRequest();
    TestIds ids = IdRegistrar.of(Sentinel.get().system()).registeredIds();
    return Arrays.asList(
        assertRequest(
            200,
            Condition.Bundle.class,
            "/api/Condition?patient={patient}&category=problem",
            ids.patient()),
        assertRequest(
            200,
            Condition.Bundle.class,
            "/api/Condition?patient={patient}&category=health-concern",
            ids.patient()),
        assertRequest(
            200,
            Condition.Bundle.class,
            "/api/Condition?patient={patient}&clinicalstatus=active",
            ids.patient()));
  }
}
