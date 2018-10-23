package gov.va.health.api.sentinel.ids;

import lombok.NoArgsConstructor;
import lombok.Value;

@Value
@NoArgsConstructor(staticName = "get")
public class SystemDefinitions {

  SystemDefinition local =
      SystemDefinition.builder()
          .ids(ServiceDefinition.builder().url("https://localhost").port(8089).build())
          .build();
}
