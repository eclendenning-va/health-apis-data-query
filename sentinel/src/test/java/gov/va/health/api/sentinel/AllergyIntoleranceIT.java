package gov.va.health.api.sentinel;

import static gov.va.health.api.sentinel.ResourceRequest.assertRequest;

import gov.va.api.health.argonaut.api.resources.AllergyIntolerance;
import gov.va.api.health.argonaut.api.resources.OperationOutcome;
import java.util.Arrays;
import java.util.List;
import org.junit.runners.Parameterized.Parameters;

public class AllergyIntoleranceIT {

  @Parameters(name = "{index}: {0} {2}")
  public static List<Object[]> parameters() {
    ResourceRequest resourceRequest = new ResourceRequest();
    TestIds ids = IdRegistrar.of(Sentinel.get().system()).registeredIds();
    return Arrays.asList(
        assertRequest(
            200,
            AllergyIntolerance.class,
            "/api/AllergyIntolerance/{id}",
            ids.allergyIntolerance()),
        assertRequest(404, OperationOutcome.class, "/api/AllergyIntolerance/{id}", ids.unknown()),
        assertRequest(
            200,
            AllergyIntolerance.Bundle.class,
            "/api/AllergyIntolerance?_id={id}",
            ids.allergyIntolerance()),
        assertRequest(
            200,
            AllergyIntolerance.Bundle.class,
            "/api/AllergyIntolerance?identifier={id}",
            ids.allergyIntolerance()),
        assertRequest(
            200,
            AllergyIntolerance.Bundle.class,
            "/api/AllergyIntolerance?patient={patient}",
            ids.patient()));
  }
}
