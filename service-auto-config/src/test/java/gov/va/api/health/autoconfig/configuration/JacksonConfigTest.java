package gov.va.api.health.autoconfig.configuration;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import java.util.function.Supplier;
import lombok.Builder;
import lombok.SneakyThrows;
import lombok.Value;
import org.junit.Test;

public class JacksonConfigTest {

  @Test
  @SneakyThrows
  public void canCreateYamlMapper() {
    ObjectMapper mapper = JacksonConfig.createMapper(new YAMLFactory());
    CandyYaml actual = mapper.readValue("ya: neat\nml: 1", CandyYaml.class);
    assertThat(actual).isEqualTo(CandyYaml.builder().ya("neat").ml(1).build());
  }

  @Test
  public void hasEasyToUseMapperSupplier() {
    Supplier<ObjectMapper> supplier = JacksonConfig::createMapper;
    assertThat(supplier.get()).isNotNull();
  }

  @Value
  @Builder
  public static class CandyYaml {
    String ya;
    int ml;
  }
}
