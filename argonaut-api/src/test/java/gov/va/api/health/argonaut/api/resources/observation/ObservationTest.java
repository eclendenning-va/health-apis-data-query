package gov.va.api.health.argonaut.api.resources.observation;

import static org.assertj.core.api.Assertions.assertThat;

import gov.va.api.health.argonaut.api.samples.SampleObservations;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;
import org.junit.Test;

public class ObservationTest {

  private SampleObservations observationsData = SampleObservations.get();

  @Test
  public void validationFailsGivenBadCategory() {
    assertThat(violationsOf(observationsData.observation())).isNotEmpty();
  }

  @Test
  public void validationFailsGivenNoCategory() {
    assertThat(violationsOf(observationsData.observation().category(null))).isNotEmpty();
  }

  @Test
  public void validationPassesGivenGoodCategory() {
    assertThat(
            violationsOf(
                observationsData
                    .observation()
                    .category()
                    .coding()
                    .get(0)
                    .code("http://hl7.org/fhir/observation-category")))
        .isEmpty();
  }

  private <T> Set<ConstraintViolation<T>> violationsOf(T object) {
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    return factory.getValidator().validate(object);
  }
}
