package gov.va.api.health.argonaut.api;

import static org.assertj.core.api.Assertions.assertThat;

import gov.va.api.health.argonaut.api.DataAbsentReason.Reason;
import lombok.SneakyThrows;
import org.junit.Test;

public class DataAbsentReasonTest {

  @Test
  @SneakyThrows
  public void reasonText() {
    assertThat(DataAbsentReason.of(Reason.not_asked).valueCode()).isEqualTo("not-asked");
    assertThat(DataAbsentReason.of(Reason.unsupported).valueCode()).isEqualTo("unsupported");
  }
}
