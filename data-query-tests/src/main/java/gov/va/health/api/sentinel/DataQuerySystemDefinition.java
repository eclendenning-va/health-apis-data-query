package gov.va.health.api.sentinel;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

/** Data-query implementation of {@link SystemDefinition}. */
@Value
@Builder
public final class DataQuerySystemDefinition implements SystemDefinition {
  @NonNull ServiceDefinition ids;

  @NonNull ServiceDefinition mrAnderson;

  @NonNull ServiceDefinition argonaut;

  @NonNull TestIds cdwIds;
}
