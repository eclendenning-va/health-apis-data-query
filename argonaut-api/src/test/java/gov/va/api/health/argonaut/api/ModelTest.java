package gov.va.api.health.argonaut.api;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import gov.va.api.health.argonaut.api.ArgonautService.SearchFailed;
import gov.va.api.health.argonaut.api.ArgonautService.UnknownResource;
import gov.va.api.health.argonaut.api.resources.OperationOutcome;
import gov.va.api.health.argonaut.api.samples.SampleAllergyIntolerances;
import gov.va.api.health.argonaut.api.samples.SampleConditions;
import gov.va.api.health.argonaut.api.samples.SampleConformance;
import gov.va.api.health.argonaut.api.samples.SampleDiagnosticReports;
import gov.va.api.health.argonaut.api.samples.SampleMedications;
import gov.va.api.health.argonaut.api.samples.SamplePatients;
import gov.va.api.health.autoconfig.configuration.JacksonConfig;
import java.util.Arrays;
import lombok.SneakyThrows;
import org.junit.Test;

public class ModelTest {

  private final SamplePatients patientData = SamplePatients.get();
  private final SampleMedications medicationData = SampleMedications.get();
  private final SampleAllergyIntolerances allergyIntoleranceData = SampleAllergyIntolerances.get();
  private final SampleDiagnosticReports diagnosticReportData = SampleDiagnosticReports.get();
  private final SampleConditions conditionData = SampleConditions.get();

  @Test
  public void allergyIntolerance() {
    roundTrip(allergyIntoleranceData.allergyIntolerance());
  }

  @Test
  public void condition() {
    roundTrip(conditionData.condition());
  }

  @Test
  public void diagnosticReport() {
    roundTrip(diagnosticReportData.diagnosticReport());
  }

  @Test
  public void conformance() {
    roundTrip(SampleConformance.get().conformance());
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

  @Test
  public void patient() {
    roundTrip(patientData.patient());
  }

  @Test
  public void range() {
    roundTrip(patientData.range());
  }

  @SneakyThrows
  private <T> void roundTrip(T object) {
    ObjectMapper mapper = new JacksonConfig().objectMapper();
    String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
    Object evilTwin = mapper.readValue(json, object.getClass());
    assertThat(evilTwin).isEqualTo(object);
  }
}
