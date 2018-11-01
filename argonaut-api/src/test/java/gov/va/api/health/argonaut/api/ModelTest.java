package gov.va.api.health.argonaut.api;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import gov.va.api.health.argonaut.api.ArgonautService.SearchFailed;
import gov.va.api.health.argonaut.api.ArgonautService.UnknownResource;
import gov.va.api.health.argonaut.api.Patient.Gender;
import gov.va.api.health.autoconfig.configuration.JacksonConfig;
import java.util.Arrays;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

@Slf4j
public class ModelTest {

  private final SampleData data = SampleData.get();

  @SuppressWarnings("ThrowableNotThrown")
  @Test
  public void exceptionConstructors() {
    new UnknownResource("some id");
    new SearchFailed("some id", "some reason");
  }

  @Test
  public void patient() {
    roundTrip(
        Patient.builder()
            .id("1234")
            .meta(data.meta())
            .implicitRules("http://HelloRules.com")
            .language("Hello Language")
            .text(data.narrative())
            .contained(singletonList(data.resource()))
            .argoRace(data.argoRaceExtension())
            .argoEthnicity(data.argoEthnicityExtension())
            .argoRace(data.argoRaceExtension())
            .argoBirthSex(data.argoBirthSexExtension())
            .modifierExtension(
                Arrays.asList(
                    data.extension(), data.extensionWithQuantity(), data.extensionWithRatio()))
            .identifier(data.identifier())
            .active(true)
            .name(singletonList(data.name()))
            .telecom(singletonList(data.telecom()))
            .gender(Gender.unknown)
            .birthDate("2000-01-01")
            .deceasedBoolean(false)
            .address(singletonList(data.address()))
            .maritalStatus(data.maritalStatus())
            .multipleBirthBoolean(false)
            .photo(singletonList(data.photo()))
            .contact(singletonList(data.contact()))
            .communication(singletonList(data.communication()))
            .careProvider(singletonList(data.reference()))
            .managingOrganization(data.reference())
            .link(singletonList(data.link()))
            .build());
  }

  @Test
  public void operationOutcome() {
    roundTrip(
        OperationOutcome.builder()
            .id("4321")
            .meta(data.meta())
            .implicitRules("http://HelloRules.com")
            .language("Hello Language")
            .text(data.narrative())
            .contained(singletonList(data.resource()))
            .modifierExtension(
                Arrays.asList(
                    data.extension(), data.extensionWithQuantity(), data.extensionWithRatio()))
            .issue(singletonList(data.issue()))
            .build());
  }

  @Test
  public void range() {
    roundTrip(data.range());
  }

  @SneakyThrows
  private <T> T roundTrip(T object) {
    ObjectMapper mapper = new JacksonConfig().objectMapper();
    String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
    log.info("{}", json);
    Object evilTwin = mapper.readValue(json, object.getClass());
    assertThat(evilTwin).isEqualTo(object);
    return object;
  }
}
