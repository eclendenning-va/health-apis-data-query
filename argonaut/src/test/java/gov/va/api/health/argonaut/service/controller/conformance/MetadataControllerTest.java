package gov.va.api.health.argonaut.service.controller.conformance;

import static org.assertj.core.api.Assertions.assertThat;

import gov.va.api.health.argonaut.api.resources.Conformance;
import gov.va.api.health.argonaut.service.controller.conformance.ConformanceStatementProperties.ContactProperties;
import gov.va.api.health.argonaut.service.controller.conformance.ConformanceStatementProperties.SecurityProperties;
import gov.va.api.health.autoconfig.configuration.JacksonConfig;
import lombok.SneakyThrows;
import org.junit.Before;
import org.junit.Test;

public class MetadataControllerTest {

  private MetadataController controller;

  @Before
  public void _init() {
    ConformanceStatementProperties properties =
        ConformanceStatementProperties.builder()
            .id("lighthouse-va-fhir-conformance")
            .version("1.4.0")
            .name("VA Lighthouse FHIR")
            .publisher("Lighthouse Team")
            .contact(
                ContactProperties.builder()
                    .name("Drew Myklegard")
                    .email("david.myklegard@va.gov")
                    .build())
            .publicationDate("2018-09-27T19:30:00-05:00")
            .description(
                "This is the base conformance statement for FHIR."
                    + " It represents a server that provides the full"
                    + " set of functionality defined by FHIR."
                    + " It is provided to use as a template for system designers to"
                    + " build their own conformance statements from.")
            .softwareName("VA Lighthouse")
            .fhirVersion("1.0.2")
            .security(
                SecurityProperties.builder()
                    .tokenEndpoint("https://argonaut.lighthouse.va.gov/token")
                    .authorizeEndpoint("https://argonaut.lighthouse.va.gov/authorize")
                    .description(
                        "This is the conformance statement to declare that the server"
                            + " supports SMART-on-FHIR. See the SMART-on-FHIR docs for the"
                            + " extension that would go with such a server.")
                    .build())
            .resourceDocumentation("Implemented per the specification")
            .build();
    controller = new MetadataController(properties);
  }

  @SneakyThrows
  private String pretty(Conformance conformance) {
    return JacksonConfig.createMapper()
        .writerWithDefaultPrettyPrinter()
        .writeValueAsString(conformance);
  }

  @Test
  @SneakyThrows
  public void read() {

    Conformance old =
        JacksonConfig.createMapper()
            .readValue(getClass().getResourceAsStream("/old-conformance.json"), Conformance.class);

    try {
      assertThat(pretty(controller.read())).isEqualTo(pretty(old));
    } catch (AssertionError e) {
      System.out.println(e.getMessage());
      throw e;
    }
  }
}
