package gov.va.api.health.argonaut.api.resources;

import static gov.va.api.health.argonaut.api.RoundTrip.assertRoundTrip;

import gov.va.api.health.argonaut.api.samples.SampleLocations;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

@Slf4j
public class LocationTest {

  private final SampleLocations data = SampleLocations.get();

  @Test
  public void location() {
    assertRoundTrip(data.location());
  }
}
