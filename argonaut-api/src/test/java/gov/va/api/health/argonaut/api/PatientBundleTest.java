package gov.va.api.health.argonaut.api;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import gov.va.api.health.argonaut.api.Patient.Bundle;
import gov.va.api.health.argonaut.api.Patient.Entry;
import gov.va.api.health.argonaut.api.Patient.Gender;
import gov.va.api.health.argonaut.api.bundle.AbstractBundle.BundleType;
import gov.va.api.health.argonaut.api.bundle.AbstractEntry;
import gov.va.api.health.argonaut.api.bundle.AbstractEntry.HttpVerb;
import gov.va.api.health.argonaut.api.bundle.AbstractEntry.Request;
import gov.va.api.health.argonaut.api.bundle.AbstractEntry.Response;
import gov.va.api.health.argonaut.api.bundle.AbstractEntry.Search;
import gov.va.api.health.argonaut.api.bundle.AbstractEntry.SearchMode;
import gov.va.api.health.argonaut.api.bundle.BundleLink;
import gov.va.api.health.argonaut.api.bundle.BundleLink.LinkRelation;
import gov.va.api.health.autoconfig.configuration.JacksonConfig;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

@Slf4j
public class PatientBundleTest {

  private SamplePatients data = SamplePatients.get();

  private Patient deleteMeAndReplaceWithProperSampleData() {
    return Patient.builder()
        .id("1234")
        .resourceType("Patient")
        .meta(data.meta())
        .implicitRules("http://HelloRules.com")
        .language("Hello Language")
        .text(data.narrative())
        .contained(singletonList(data.resource()))
        .extension(Arrays.asList(data.extension(), data.extension()))
        .modifierExtension(
            Arrays.asList(
                data.extension(), data.extensionWithQuantity(), data.extensionWithRatio()))
        .identifier(singletonList(data.identifier()))
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
        .build();
  }

  @Test
  public void maybeThisWorks() {
    Entry entry =
        Entry.builder()
            .extension(Collections.singletonList(data.extension()))
            .fullUrl("http://patient.com")
            .id("123")
            .link(
                Collections.singletonList(
                    BundleLink.builder()
                        .relation(LinkRelation.self)
                        .url(("http://patient.com/1"))
                        .build()))
            .resource(deleteMeAndReplaceWithProperSampleData())
            .search(
                Search.builder()
                    .id("s1")
                    .mode(SearchMode.match)
                    .extension(singletonList(data.extension()))
                    .modifierExtension(singletonList(data.extension()))
                    .rank(new BigDecimal(0.5))
                    .build())
            .request(
                Request.builder()
                    .id("request1")
                    .extension(singletonList(data.extension()))
                    .modifierExtension(singletonList(data.extension()))
                    .method(HttpVerb.GET)
                    .url("http://example.com")
                    .ifNoneMatch("ok")
                    .ifModifiedSince("also ok")
                    .ifMatch("really ok")
                    .ifNoneExist("meh, ok.")
                    .build())
            .response(
                Response.builder()
                    .id("request1")
                    .extension(singletonList(data.extension()))
                    .modifierExtension(singletonList(data.extension()))
                    .status("single")
                    .location("http://example.com")
                    .etag("you're it")
                    .lastModified("2005-01-21T07:57:00Z")
                    .build())
            .build();

    Bundle bundle =
        Bundle.builder()
            .entry(Collections.singletonList(entry))
            .link(
                Collections.singletonList(
                    BundleLink.builder()
                        .relation(LinkRelation.self)
                        .url(("http://patient.com/2"))
                        .build()))
            .type(BundleType.searchset)
            .build();

    roundTrip(bundle);

    AbstractEntry.Search.builder().build().id();
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
