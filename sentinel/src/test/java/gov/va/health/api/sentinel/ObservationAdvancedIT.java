package gov.va.health.api.sentinel;

import static gov.va.health.api.sentinel.ResourceRequest.assertRequest;

import gov.va.api.health.argonaut.api.resources.Observation;
import java.util.Arrays;
import java.util.List;
import org.junit.runners.Parameterized.Parameters;

public class ObservationAdvancedIT {

  @Parameters(name = "{index}: {0} {2}")
  public static List<Object[]> parameters() {
    ResourceRequest resourceRequest = new ResourceRequest();
    TestIds ids = IdRegistrar.of(Sentinel.get().system()).registeredIds();
    return Arrays.asList(
        assertRequest(
            200,
            Observation.Bundle.class,
            "/api/Observation?patient={patient}&category=laboratory",
            ids.patient()),
        assertRequest(
            200,
            Observation.Bundle.class,
            "/api/Observation?patient={patient}&category=laboratory&date={date}",
            ids.patient(),
            ids.observations().onDate()),
        assertRequest(
            200,
            Observation.Bundle.class,
            "/api/Observation?patient={patient}&category=laboratory&date={from}&date={to}",
            ids.patient(),
            ids.observations().dateRange().from(),
            ids.observations().dateRange().to()),
        assertRequest(
            200,
            Observation.Bundle.class,
            "/api/Observation?patient={patient}&category=vital-signs",
            ids.patient()),
        assertRequest(
            200,
            Observation.Bundle.class,
            "/api/Observation?patient={patient}&category=laboratory,vital-signs",
            ids.patient()),
        assertRequest(
            200,
            Observation.Bundle.class,
            "/api/Observation?patient={patient}&code={loinc1}",
            ids.patient(),
            ids.observations().loinc1()),
        assertRequest(
            200,
            Observation.Bundle.class,
            "/api/Observation?patient={patient}&code={loinc1},{loinc2}",
            ids.patient(),
            ids.observations().loinc1(),
            ids.observations().loinc2()));
  }
}
