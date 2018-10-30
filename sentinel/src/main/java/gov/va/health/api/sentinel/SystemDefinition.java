package gov.va.health.api.sentinel;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;

/** Specifies the particulars for interacting with the different services within a system. */
@Value
@Builder
@Slf4j
public class SystemDefinition {
  @NonNull ServiceDefinition ids;
  @NonNull ServiceDefinition mrAnderson;
  @NonNull TestIds cdwIds;

  public TestClients clients() {
    return TestClients.of(this);
  }
}
