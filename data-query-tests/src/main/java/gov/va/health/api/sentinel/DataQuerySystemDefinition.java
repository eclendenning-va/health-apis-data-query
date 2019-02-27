package gov.va.health.api.sentinel;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

/**
 * Data-query implementation of {@link SystemDefinition} with service definitions for ids,
 * mr-anderson, and argonaut.
 */
@Value
@Builder
public final class DataQuerySystemDefinition implements SystemDefinition {
  @NonNull ServiceDefinition ids;

  @NonNull ServiceDefinition mrAnderson;

  @NonNull ServiceDefinition argonaut;

  @NonNull TestIds cdwIds;
}
