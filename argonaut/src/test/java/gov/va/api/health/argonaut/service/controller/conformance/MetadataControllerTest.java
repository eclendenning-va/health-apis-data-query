package gov.va.api.health.argonaut.service.controller.conformance;

import static org.assertj.core.api.Assertions.assertThat;

import gov.va.api.health.argonaut.api.resources.Conformance;
import gov.va.api.health.autoconfig.configuration.JacksonConfig;
import lombok.SneakyThrows;
import org.junit.Before;
import org.junit.Test;

public class MetadataControllerTest {

  MetadataController controller;

  @Before
  public void _init() {
    ConformanceStatementProperties properties = ConformanceStatementProperties.builder().build();
    controller = new MetadataController(properties);
  }

  @Test
  @SneakyThrows
  public void read() {

    Conformance old =
        JacksonConfig.createMapper()
            .readValue(getClass().getResourceAsStream("/old-conformance.json"), Conformance.class);

    assertThat(controller.read()).isEqualTo(old);
  }
}
