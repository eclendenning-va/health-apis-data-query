package gov.va.api.health.argonaut.api.resources;

import static gov.va.api.health.argonaut.api.RoundTrip.assertRoundTrip;

import gov.va.api.health.argonaut.api.samples.SampleOrganizations;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

@Slf4j
public class OrganizationTest {
  private final SampleOrganizations data = SampleOrganizations.get();

  @Test
  public void organization() {
    assertRoundTrip(data.organization());
  }
}
