package gov.va.health.api.sentinel;

import static gov.va.health.api.sentinel.ResourceRequest.assertRequest;

import gov.va.api.health.argonaut.api.resources.Patient;
import java.util.Arrays;
import java.util.List;
import org.junit.runners.Parameterized.Parameters;

public class PatientAdvancedIT {

  @Parameters(name = "{index}: {0} {2}")
  public static List<Object[]> parameters() {
    ResourceRequest resourceRequest = new ResourceRequest();
    TestIds ids = IdRegistrar.of(Sentinel.get().system()).registeredIds();
    return Arrays.asList(
        assertRequest(
            200,
            Patient.Bundle.class,
            "/api/Patient?family={family}&gender={gender}",
            ids.pii().family(),
            ids.pii().gender()),
        assertRequest(
            200,
            Patient.Bundle.class,
            "/api/Patient?given={given}&gender={gender}",
            ids.pii().given(),
            ids.pii().gender()),
        assertRequest(
            200,
            Patient.Bundle.class,
            "/api/Patient?name={name}&birthdate={birthdate}",
            ids.pii().name(),
            ids.pii().birthdate()),
        assertRequest(
            200,
            Patient.Bundle.class,
            "/api/Patient?name={name}&gender={gender}",
            ids.pii().name(),
            ids.pii().gender()));
  }
}
