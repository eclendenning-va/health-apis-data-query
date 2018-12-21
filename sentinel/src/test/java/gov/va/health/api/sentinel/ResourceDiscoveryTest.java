package gov.va.health.api.sentinel;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;

import gov.va.api.health.argonaut.api.resources.Conformance;
import gov.va.api.health.argonaut.api.resources.Conformance.Rest;
import gov.va.api.health.argonaut.api.resources.Conformance.RestResource;
import gov.va.api.health.argonaut.api.resources.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import lombok.NoArgsConstructor;
import org.junit.Test;

public class ResourceDiscoveryTest {
  ResourceDiscovery resourceDiscovery = new ResourceDiscovery();
  private final ConformanceTestData data = ConformanceTestData.get();

  @Test
  public void invalidRestResources() {
    assertThat(resourceDiscovery.extractRestResources(null)).isNull();
    assertThat(resourceDiscovery.extractRestResources(data.noRestListConformanceStatement)).isNull();

  }

  @NoArgsConstructor(staticName = "get")
  public static class ConformanceTestData {
    Conformance noRestListConformanceStatement = Conformance.builder().build();

  }
}
