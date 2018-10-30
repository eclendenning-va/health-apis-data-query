package gov.va.health.api.sentinel;

import lombok.Builder;
import lombok.Value;

/** Specifies the particulars for interacting with the different services within a system. */
@Value
@Builder
public class SystemDefinition {
  ServiceDefinition ids;
}
