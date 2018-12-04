package gov.va.api.health.argonaut.api.resources;

import static gov.va.api.health.argonaut.api.RoundTrip.assertRoundTrip;

import gov.va.api.health.argonaut.api.samples.SamplePractitioners;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

@Slf4j
public class PractitionerTest {

  private final SamplePractitioners data = SamplePractitioners.get();

  @Test
  public void practitioner() {
    assertRoundTrip(data.practitioner());
  }
}
