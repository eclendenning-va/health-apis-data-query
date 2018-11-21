package gov.va.api.health.argonaut.api.resources;

import static gov.va.api.health.argonaut.api.RoundTrip.assertRoundTrip;

import gov.va.api.health.argonaut.api.samples.SampleConformance;
import org.junit.Test;

public class ConformanceTest {

  @Test
  public void conformance() {
    assertRoundTrip(SampleConformance.get().conformance());
  }
}
