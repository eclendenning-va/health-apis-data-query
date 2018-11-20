package gov.va.api.health.argonaut.api.resources.diagnosticreport;

import static org.assertj.core.api.Assertions.assertThat;

import gov.va.api.health.argonaut.api.samples.SampleDiagnosticReports;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;
import org.junit.Test;

public class DiagnosticReportTest {

  private SampleDiagnosticReports diagnosticReportsData = SampleDiagnosticReports.get();

  @Test
  public void validationFailsGivenBadCategory() {
    assertThat(violationsOf(diagnosticReportsData.diagnosticReport())).isNotEmpty();
  }

  @Test
  public void validationFailsGivenNoCategory() {
    assertThat(violationsOf(diagnosticReportsData.diagnosticReport().category(null))).isNotEmpty();
  }

  @Test
  public void validationPassesGivenGoodCategory() {
    assertThat(
            violationsOf(
                diagnosticReportsData
                    .diagnosticReport()
                    .category()
                    .coding()
                    .get(0)
                    .code("http://hl7.org/fhir/ValueSet/diagnostic-service-sections")))
        .isEmpty();
  }

  private <T> Set<ConstraintViolation<T>> violationsOf(T object) {
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    return factory.getValidator().validate(object);
  }
}
