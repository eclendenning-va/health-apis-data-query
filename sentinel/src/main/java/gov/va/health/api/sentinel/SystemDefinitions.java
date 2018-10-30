package gov.va.health.api.sentinel;

import lombok.NoArgsConstructor;
import lombok.Value;

/** The standard system configurations for typical environments like QA or PROD. */
@Value
@NoArgsConstructor(staticName = "get")
public class SystemDefinitions {

  SystemDefinition local =
      SystemDefinition.builder()
          .ids(ServiceDefinition.builder().url("https://localhost").port(8089).build())
          .build();
}
