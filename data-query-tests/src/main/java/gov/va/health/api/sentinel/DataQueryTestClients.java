package gov.va.health.api.sentinel;

import gov.va.api.health.autoconfig.configuration.JacksonConfig;
import lombok.experimental.UtilityClass;

/** Test clients for interacting with different services in a {@link DataQuerySystemDefinition}. */
@UtilityClass
public final class DataQueryTestClients {
  static TestClient argonaut() {
    return FhirTestClient.builder()
        .service(systemDefinition().argonaut())
        .mapper(JacksonConfig::createMapper)
        .build();
  }

  static TestClient ids() {
    return BasicTestClient.builder()
        .service(systemDefinition().ids())
        .contentType("application/json")
        .mapper(JacksonConfig::createMapper)
        .build();
  }

  static TestClient mrAnderson() {
    return BasicTestClient.builder()
        .service(systemDefinition().mrAnderson())
        .contentType("application/xml")
        .mapper(JacksonConfig::createMapper)
        .build();
  }

  private static DataQuerySystemDefinition systemDefinition() {
    return DataQuerySystemDefinitions.get().systemDefinition();
  }
}
