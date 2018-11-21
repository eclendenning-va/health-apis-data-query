package gov.va.api.health.argonaut.api.validation;

import gov.va.api.health.argonaut.api.ZeroOrOneVerifier;
import gov.va.api.health.argonaut.api.samples.SampleDataTypes;
import org.junit.Test;

public class AnnotationValidationTest {
  private final SampleDataTypes data = SampleDataTypes.get();

  @Test
  public void relatedGroups() {
    ZeroOrOneVerifier.builder().sample(data.annotation()).fieldPrefix("author").build().verify();
  }
}
