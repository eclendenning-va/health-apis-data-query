package gov.va.api.health.argonaut.api.resources;

import static gov.va.api.health.argonaut.api.RoundTrip.assertRoundTrip;

import gov.va.api.health.argonaut.api.samples.SampleAppointments;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

@Slf4j
public class AppointmentTest {

  private final SampleAppointments data = SampleAppointments.get();

  @Test
  public void appointment() {
    assertRoundTrip(data.appointment());
  }
}
