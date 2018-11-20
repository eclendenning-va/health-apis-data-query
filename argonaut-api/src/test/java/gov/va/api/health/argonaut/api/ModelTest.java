package gov.va.api.health.argonaut.api;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import gov.va.api.health.argonaut.api.ArgonautService.SearchFailed;
import gov.va.api.health.argonaut.api.ArgonautService.UnknownResource;
import gov.va.api.health.argonaut.api.resources.OperationOutcome;
import gov.va.api.health.argonaut.api.samples.SampleAllergyIntolerances;
import gov.va.api.health.argonaut.api.samples.SampleDiagnosticReports;
import gov.va.api.health.argonaut.api.samples.SampleMedications;
import gov.va.api.health.argonaut.api.samples.SamplePatients;
import gov.va.api.health.autoconfig.configuration.JacksonConfig;
import java.util.Arrays;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

@Slf4j
public class ModelTest {

  private final SamplePatients patientData = SamplePatients.get();
  private final SampleMedications medicationData = SampleMedications.get();
  private final SampleAllergyIntolerances allergyIntoleranceData = SampleAllergyIntolerances.get();
  private final SampleDiagnosticReports diagnosticReportData = SampleDiagnosticReports.get();

  @Test
  public void diagnosticReport() {
    roundTrip(diagnosticReportData.diagnosticReport());
  }

  @SuppressWarnings("ThrowableNotThrown")
  @Test
  public void exceptionConstructors() {
    new UnknownResource("some id");
    new SearchFailed("some id", "some reason");
  }

  @Test
  public void medication() {
    roundTrip(medicationData.medication());
  }

  @Test
  public void operationOutcome() {
    roundTrip(
        OperationOutcome.builder()
            .id("4321")
            .meta(patientData.meta())
            .implicitRules("http://HelloRules.com")
            .language("Hello Language")
            .text(patientData.narrative())
            .contained(singletonList(patientData.resource()))
            .modifierExtension(
                Arrays.asList(
                    patientData.extension(),
                    patientData.extensionWithQuantity(),
                    patientData.extensionWithRatio()))
            .issue(singletonList(patientData.issue()))
            .build());
  }

  /*
   Believe this test to be failing due to a java bean property.
   it is viewing the "isBrand" variable as a method and creating its own "brand" field
   This may be fixed with a JsonProperty similar to "package" - however, package is not completely ironed out either
  */

  @Test
  public void patient() {
    roundTrip(patientData.patient());
  }

  @Test
  public void range() {
    roundTrip(patientData.range());
  }

  @Test
  public void allergyIntolerance() {
    roundTrip(allergyIntoleranceData.allergyIntolerance());
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
