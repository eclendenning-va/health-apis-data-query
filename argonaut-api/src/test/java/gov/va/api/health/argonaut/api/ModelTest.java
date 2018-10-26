package gov.va.api.health.argonaut.api;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import gov.va.api.health.argonaut.api.Address.AddressType;
import gov.va.api.health.argonaut.api.Address.AddressUse;
import gov.va.api.health.argonaut.api.ArgonautService.SearchFailed;
import gov.va.api.health.argonaut.api.ArgonautService.UnknownResource;
import gov.va.api.health.argonaut.api.ContactPoint.ContactPointSystem;
import gov.va.api.health.argonaut.api.ContactPoint.ContactPointUse;
import gov.va.api.health.argonaut.api.HumanName.NameUse;
import gov.va.api.health.argonaut.api.Identifier.IdentifierUse;
import gov.va.api.health.argonaut.api.Narrative.NarrativeStatus;
import gov.va.api.health.argonaut.api.Patient.Gender;
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
            .text(testNarrative)
            .contained(Arrays.asList(testResource))
            .argoRace(testArgoRaceExtension)
            .argoEthnicity(testArgoEthnicityExtension)
            .argoRace(testArgoRaceExtension)
            .argoBirthSex(testArgoBirthSexExtension)
            .modifierExtension(Arrays.asList(testExtension))
            .identifier(testIdentifier)
            .active(true)
            .name(Arrays.asList(testName))
            .telecom(Arrays.asList(testTelecom))
            .gender(Gender.unknown)
            .birthDate("2000-01-01")
            .deceasedBoolean(false)
            .address(Arrays.asList(testAddress))
            .maritalStatus(testMaritalStatus)
            .multipleBirthBoolean(false)
            .photo(Arrays.asList(testPhoto))
            .contact(Arrays.asList(testContact))
            .communication(Arrays.asList(testCommunication))
            .careProvider(Arrays.asList(testReference))
            .managingOrganization(testReference)
            .link(Arrays.asList(testLink))
            .build());
  }

  @Test
  public void range() {
    roundTrip(testRange);
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

  Period testPeriod =
      Period.builder()
          .id("5678")
          .extension(
              Arrays.asList(Extension.builder().url("http://wtf.com").valueInteger(1).build()))
          .start("2000-01-01T00:00:00-00:00")
          .end("2001-01-01T00:00:00-00:00")
          .build();

  HumanName testName =
      HumanName.builder()
          .use(NameUse.anonymous)
          .text("HelloText")
          .family(Arrays.asList("HelloFamily"))
          .given(Arrays.asList("HelloGiven"))
          .prefix(Arrays.asList("HelloPrefix"))
          .suffix(Arrays.asList("HelloSuffix"))
          .period(testPeriod)
          .build();

  CodeableConcept testMaritalStatus =
      CodeableConcept.builder().coding(Arrays.asList(testCoding)).text("HelloText").build();

  CodeableConcept testLanguage =
      CodeableConcept.builder().coding(Arrays.asList(testCoding)).text("HelloText").build();

  CodeableConcept testRelationship =
      CodeableConcept.builder().coding(Arrays.asList(testCoding)).text("HelloText").build();

  Address testAddress =
      Address.builder()
          .id("1234")
          .extension(Arrays.asList(testExtension))
          .use(AddressUse.home)
          .type(AddressType.both)
          .text("Hello")
          .line(Arrays.asList("hello", "goodbye"))
          .city("Hello City")
          .district("Hello District")
          .state("Hello State")
          .postalCode("12345")
          .country("Hello Country")
          .period(testPeriod)
          .build();

  ContactPoint testTelecom =
      ContactPoint.builder()
          .system(ContactPointSystem.other)
          .value("HelloValue")
          .use(ContactPointUse.home)
          .rank(1)
          .period(testPeriod)
          .build();

  Reference testReference =
      Reference.builder().reference("HelloReference").display("HelloDisplay").build();

  Attachment testPhoto =
      Attachment.builder()
          .contentType("HelloType")
          .language("HelloLanguage")
          .data("HelloData")
          .url("http://HelloUrl.com")
          .size(1)
          .hash("HelloHash")
          .title("HelloTitle")
          .creation("2000-01-01T00:00:00-00:00")
          .build();

  Contact testContact =
      Contact.builder()
          .id("0000")
          .extension(Arrays.asList(testExtension))
          .modifierExtension(Arrays.asList(testExtension))
          .relationship(Arrays.asList(testRelationship))
          .name(testName)
          .telecom(Arrays.asList(testTelecom))
          .address(testAddress)
          .gender(Contact.Gender.unknown)
          .organization(testReference)
          .period(testPeriod)
          .build();

  Communication testCommunication =
      Communication.builder()
          .id("8888")
          .extension(Arrays.asList(testExtension))
          .modifierExtension(Arrays.asList(testExtension))
          .language(testLanguage)
          .preferred(false)
          .build();

  Link testLink =
      Link.builder()
          .id("7777")
          .extension(Arrays.asList(testExtension))
          .modifierExtension(Arrays.asList(testExtension))
          .other(testReference)
          .type("HelloType")
          .build();

  ArgoRaceExtension testArgoRaceExtension =
      ArgoRaceExtension.builder()
          .id("2222")
          .extension(Arrays.asList(testExtension))
          .url("http://HelloUrl.com")
          .build();
  ArgoEthnicityExtension testArgoEthnicityExtension =
      ArgoEthnicityExtension.builder()
          .id("3333")
          .extension(Arrays.asList(testExtension))
          .url("http://HelloUrl.com")
          .build();
  ArgoBirthSexExtension testArgoBirthSexExtension =
      ArgoBirthSexExtension.builder()
          .id("4444")
          .extension(Arrays.asList(testExtension))
          .url("http://HelloUrl.com")
          .valueCode("HelloCode")
          .build();

  SimpleQuantity testSimpleQuantity =
      SimpleQuantity.builder().value(11.11).unit("HelloUnit").build();

  Range testRange = Range.builder().low(testSimpleQuantity).high(testSimpleQuantity).build();

  @SuppressWarnings("ThrowableNotThrown")
  @Test
  public void exceptionConstructors() {
    new UnknownResource("some id");
    new SearchFailed("some id", "some reason");
  }
}
