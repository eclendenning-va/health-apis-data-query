package gov.va.api.health.argonaut.api;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import gov.va.api.health.argonaut.api.ArgonautService.SearchFailed;
import gov.va.api.health.argonaut.api.ArgonautService.UnknownResource;
import gov.va.api.health.argonaut.api.Fhir.Extension;
import gov.va.api.health.argonaut.api.Identifier.IdentifierUse;
import gov.va.api.health.autoconfig.configuration.JacksonConfig;
import java.util.Arrays;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

@Slf4j
public class ModelTest {

  @SneakyThrows
  private <T> T roundTrip(T object) {
    ObjectMapper mapper = new JacksonConfig().objectMapper();
    String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
    log.info("{}", json);
    Object evilTwin = mapper.readValue(json, object.getClass());
    assertThat(evilTwin).isEqualTo(object);
    return object;
  }

  @Test
  public void patient() {
    roundTrip(
        Patient.builder()
            .id("1234")
            .identifier(
                Identifier.builder()
                    .id("5678")
                    .use(IdentifierUse.official)
                    .extension(
                        Arrays.asList(
                            Extension.builder().url("http://wtf.com").valueInteger(1).build()))
                    .build())
            .build());
  }

  @SuppressWarnings("ThrowableNotThrown")
  @Test
  public void exceptionConstructors() {
    new UnknownResource("some id");
    new SearchFailed("some id", "some reason");
  }
}
