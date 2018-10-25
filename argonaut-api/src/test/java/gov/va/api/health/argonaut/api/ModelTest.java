package gov.va.api.health.argonaut.api;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import gov.va.api.health.argonaut.api.Address.AddressType;
import gov.va.api.health.argonaut.api.Address.AddressUse;
import gov.va.api.health.argonaut.api.ArgonautService.SearchFailed;
import gov.va.api.health.argonaut.api.ArgonautService.UnknownResource;
import gov.va.api.health.argonaut.api.Identifier.IdentifierUse;
import gov.va.api.health.argonaut.api.Narrative.NarrativeStatus;
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
            .meta(testMeta)
            .implicitRules("http://HelloRules.com")
            .language("Hello Language")
            .identifier(testIdentifier)
            .text(testNarrative)
            .build());
  }

  @Test
  public void address() {
    roundTrip(testAddress);
  }

  Coding testCoding =
      Coding.builder()
          .system("http://HelloSystem.com")
          .version("Hello Version")
          .code("Hello Code")
          .display("Hello Display")
          .userSelected(true)
          .build();

  Meta testMeta =
      Meta.builder()
          .versionId("1111")
          .lastUpdated("2000-01-01T00:00:00-00:00")
          .profile(Arrays.asList("http://HelloProfile.com"))
          .security(Arrays.asList(testCoding))
          .tag(Arrays.asList(testCoding))
          .build();

  Resource testResource =
      Resource.builder()
          .id("1111")
          .meta(testMeta)
          .implicitRules("http://HelloRules.com")
          .language("Hello Language")
          .build();

  Narrative testNarrative =
      Narrative.builder().status(NarrativeStatus.additional).div("<p>HelloDiv<p>").build();

  Extension testExtension = Extension.builder().url("http://HelloUrl.com").valueInteger(1).build();

  Identifier testIdentifier =
      Identifier.builder()
          .id("5678")
          .use(IdentifierUse.official)
          .extension(Arrays.asList(testExtension))
          .build();

  Address testAddress =
      Address.builder()
          .id("1234")
          .extension(
              Arrays.asList(Extension.builder().url("http://wtf.com").valueInteger(1).build()))
          .use(AddressUse.home)
          .type(AddressType.both)
          .text("Hello")
          .line(Arrays.asList("hello", "goodbye"))
          .city("Hello City")
          .district("Hello District")
          .state("Hello State")
          .postalCode("12345")
          .country("Hello Country")
          .period(
              Period.builder()
                  .id("5678")
                  .extension(
                      Arrays.asList(
                          Extension.builder().url("http://wtf.com").valueInteger(1).build()))
                  .start("2000-01-01T00:00:00-00:00")
                  .end("2001-01-01T00:00:00-00:00")
                  .build())
          .build();

  @SuppressWarnings("ThrowableNotThrown")
  @Test
  public void exceptionConstructors() {
    new UnknownResource("some id");
    new SearchFailed("some id", "some reason");
  }
}
