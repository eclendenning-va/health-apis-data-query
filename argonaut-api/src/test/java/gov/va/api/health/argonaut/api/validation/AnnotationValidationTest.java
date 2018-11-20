package gov.va.api.health.argonaut.api.validation;

import static org.assertj.core.api.Assertions.assertThat;

import gov.va.api.health.argonaut.api.samples.SampleDataTypes;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;
import org.junit.Test;

public class AnnotationValidationTest {
  private final SampleDataTypes data = SampleDataTypes.get();

  @Test
  public void annotationWithBothAuthorValuesIsNotValid() {
    assertThat(violationsOf(data.annotationWithBothAuthorValues())).isNotEmpty();
  }

  @Test
  public void annotationWithOneAuthorValueIsValid() {
    assertThat(violationsOf(data.annotation())).isEmpty();
  }

  private <T> Set<ConstraintViolation<T>> violationsOf(T object) {
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    return factory.getValidator().validate(object);
  }
}
