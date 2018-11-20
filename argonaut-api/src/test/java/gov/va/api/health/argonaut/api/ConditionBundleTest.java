package gov.va.api.health.argonaut.api;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import gov.va.api.health.argonaut.api.bundle.AbstractBundle;
import gov.va.api.health.argonaut.api.bundle.AbstractEntry;
import gov.va.api.health.argonaut.api.bundle.BundleLink;
import gov.va.api.health.argonaut.api.resources.Condition;
import gov.va.api.health.argonaut.api.samples.SampleConditions;
import gov.va.api.health.argonaut.api.samples.SampleDataTypes;
import gov.va.api.health.autoconfig.configuration.JacksonConfig;
import java.util.Collections;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

@Slf4j
public class ConditionBundleTest {

  private final SampleConditions conditionData = SampleConditions.get();
  private final SampleDataTypes dataTypes = SampleDataTypes.get();

  @Test
  public void bundlerCanBuildConditionBundles() {
    Condition.Entry entry =
        Condition.Entry.builder()
            .extension(Collections.singletonList(conditionData.extension()))
            .fullUrl("http://condition.com")
            .id("123")
            .link(
                Collections.singletonList(
                    BundleLink.builder()
                        .relation(BundleLink.LinkRelation.self)
                        .url(("http://condition/1"))
                        .build()))
            .resource(conditionData.condition())
            .search(dataTypes.search())
            .request(dataTypes.request())
            .response(dataTypes.response())
            .build();

    Condition.Bundle bundle =
        Condition.Bundle.builder()
            .entry(Collections.singletonList(entry))
            .link(
                Collections.singletonList(
                    BundleLink.builder()
                        .relation(BundleLink.LinkRelation.self)
                        .url(("http://condition.com/2"))
                        .build()))
            .type(AbstractBundle.BundleType.searchset)
            .build();

    roundTrip(bundle);
    AbstractEntry.Search.builder().build().id();
  }

  @SneakyThrows
  private <T> void roundTrip(T object) {
    ObjectMapper mapper = new JacksonConfig().objectMapper();
    String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
    log.info("{}", json);
    Object evilTwin = mapper.readValue(json, object.getClass());
    assertThat(evilTwin).isEqualTo(object);
  }
}
