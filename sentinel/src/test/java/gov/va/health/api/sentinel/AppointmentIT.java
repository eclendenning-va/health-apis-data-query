package gov.va.health.api.sentinel;

import gov.va.api.health.argonaut.api.resources.Appointment;
import gov.va.api.health.argonaut.api.resources.OperationOutcome;
import java.util.Arrays;
import java.util.List;
import org.junit.runners.Parameterized.Parameters;

public class AppointmentIT {

  @Parameters(name = "{index}: {0} {2}")
  public static List<Object[]> parameters() {
    ResourceRequest resourceRequest = new ResourceRequest();
    TestIds ids = IdRegistrar.of(Sentinel.get().system()).registeredIds();
    return Arrays.asList(
        resourceRequest.assertRequest(
            200, Appointment.class, "/api/Appointment/{id}", ids.appointment()),
        resourceRequest.assertRequest(
            404, OperationOutcome.class, "/api/Appointment/{id}", ids.unknown()),
        resourceRequest.assertRequest(
            200, Appointment.Bundle.class, "/api/Appointment?_id={id}", ids.appointment()),
        resourceRequest.assertRequest(
            200, Appointment.Bundle.class, "/api/Appointment?identifier={id}", ids.appointment()),
        resourceRequest.assertRequest(
            200, Appointment.Bundle.class, "/api/Appointment?patient={patient}", ids.patient()));
  }
}
