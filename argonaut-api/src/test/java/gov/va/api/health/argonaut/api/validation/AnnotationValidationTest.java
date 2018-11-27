package gov.va.api.health.argonaut.api.validation;

import gov.va.api.health.argonaut.api.ZeroOrOneOfVerifier;
import gov.va.api.health.argonaut.api.samples.SampleDataTypes;
import org.junit.Test;

public class AnnotationValidationTest {
  private final SampleDataTypes data = SampleDataTypes.get();

  @Test
  public void relatedGroups() {
    ZeroOrOneOfVerifier.builder().sample(data.annotation()).fieldPrefix("author").build().verify();
  }
}
