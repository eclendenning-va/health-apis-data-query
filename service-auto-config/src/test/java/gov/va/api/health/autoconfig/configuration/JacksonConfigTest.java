package gov.va.api.health.autoconfig.configuration;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.function.Supplier;
import org.junit.Test;

public class JacksonConfigTest {

  @Test
  public void hasEasyToUseMapperSupplier() {
    Supplier<ObjectMapper> supplier = JacksonConfig::createMapper;
    assertThat(supplier.get()).isNotNull();
  }
}
