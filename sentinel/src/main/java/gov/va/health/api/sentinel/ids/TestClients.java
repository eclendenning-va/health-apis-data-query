package gov.va.health.api.sentinel.ids;

import gov.va.api.health.autoconfig.configuration.JacksonConfig;
import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor(staticName = "of")
public class TestClients {

  SystemDefinition systemDefinition;

  TestClient ids() {
    return BasicTestClient.builder()
        .service(systemDefinition.ids())
        .contentType("application/json")
        .mapper(JacksonConfig::createMapper)
        .build();
  }
}
