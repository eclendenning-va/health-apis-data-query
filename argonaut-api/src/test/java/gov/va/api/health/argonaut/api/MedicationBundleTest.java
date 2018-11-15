package gov.va.api.health.argonaut.api;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import gov.va.api.health.argonaut.api.bundle.AbstractBundle;
import gov.va.api.health.argonaut.api.bundle.AbstractEntry;
import gov.va.api.health.argonaut.api.bundle.BundleLink;
import gov.va.api.health.autoconfig.configuration.JacksonConfig;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

@Slf4j
public class MedicationBundleTest {

  private SampleMedications data = SampleMedications.get();

  private Medication testMedication() {
    return Medication.builder()
        .id("1234")
        .resourceType("Medication")
        .meta(data.meta())
        .implicitRules("http://medicationRules.com")
        .language("Medication Language")
        .text(data.narrative())
        .contained(Collections.singletonList(data.resource()))
        .extension(Arrays.asList(data.extension(), data.extension(), data.extension()))
        .modifierExtension(
            Arrays.asList(
                data.extension(), data.extensionWithQuantity(), data.extensionWithRatio()))
        .code(data.code())
        .isBrand(false)
        .manufacturer(data.reference())
        .product(data.product())
        .medicationPackage(data.medicationPackage())
        .build();
  }

  @Test
  public void bundlerCanBuildMedicationBundles() {
    Medication.Entry entry =
        Medication.Entry.builder()
            .extension(Collections.singletonList(data.extension()))
            .fullUrl("http://medication.com")
            .id("123")
            .link(
                Collections.singletonList(
                    BundleLink.builder()
                        .relation(BundleLink.LinkRelation.self)
                        .url(("http://medication.com/1"))
                        .build()))
            .resource(testMedication())
            .search(
                AbstractEntry.Search.builder()
                    .id("s1")
                    .mode(AbstractEntry.SearchMode.match)
                    .extension(singletonList(data.extension()))
                    .modifierExtension(singletonList(data.extension()))
                    .rank(new BigDecimal(0.5))
                    .build())
            .request(
                AbstractEntry.Request.builder()
                    .id("request1")
                    .extension(singletonList(data.extension()))
                    .modifierExtension(singletonList(data.extension()))
                    .method(AbstractEntry.HttpVerb.GET)
                    .url("http://example.com")
                    .ifNoneMatch("ok")
                    .ifModifiedSince("also ok")
                    .ifMatch("really ok")
                    .ifNoneExist("meh, ok.")
                    .build())
            .response(
                AbstractEntry.Response.builder()
                    .id("request1")
                    .extension(singletonList(data.extension()))
                    .modifierExtension(singletonList(data.extension()))
                    .status("single")
                    .location("http://example.com")
                    .etag("you're it")
                    .lastModified("2005-01-21T07:57:00Z")
                    .build())
            .build();

    Medication.Bundle bundle =
        Medication.Bundle.builder()
            .entry(Collections.singletonList(entry))
            .link(
                Collections.singletonList(
                    BundleLink.builder()
                        .relation(BundleLink.LinkRelation.self)
                        .url(("http://medication.com/2"))
                        .build()))
            .type(AbstractBundle.BundleType.searchset)
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
