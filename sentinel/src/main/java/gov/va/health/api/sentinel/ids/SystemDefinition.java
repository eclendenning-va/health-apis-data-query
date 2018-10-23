package gov.va.health.api.sentinel.ids;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class SystemDefinition {
  ServiceDefinition ids;
}
