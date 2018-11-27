package gov.va.health.api.sentinel;

import gov.va.api.health.argonaut.api.resources.AllergyIntolerance;
import gov.va.api.health.argonaut.api.resources.Condition;
import gov.va.api.health.argonaut.api.resources.DiagnosticReport;
import gov.va.api.health.argonaut.api.resources.Immunization;
import gov.va.api.health.argonaut.api.resources.Medication;
import gov.va.api.health.argonaut.api.resources.Observation;
import gov.va.api.health.argonaut.api.resources.OperationOutcome;
import gov.va.api.health.argonaut.api.resources.Patient;
import java.util.Arrays;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

@SuppressWarnings({"DefaultAnnotationParam", "WeakerAccess"})
@RunWith(Parameterized.class)
@Slf4j
public class ArgonautReadAndSearchIT {

  @Parameter(0)
  public int status;

  @Parameter(1)
  public Class<?> response;

  @Parameter(2)
  public String path;

  @Parameter(3)
  public String[] params;

  public static Object[] expect(int status, Class<?> response, String path, String... parameters) {
    return new Object[] {status, response, path, parameters};
  }

  @Parameters(name = "{index}: {0} {2}")
  public static List<Object[]> parameters() {
    TestIds ids = IdRegistrar.of(Sentinel.get().system()).registeredIds();
    return Arrays.asList(
        // Allergy Intolerance
        expect(
            200,
            AllergyIntolerance.class,
            "/api/AllergyIntolerance/{id}",
            ids.allergyIntolerance()),
        expect(404, OperationOutcome.class, "/api/AllergyIntolerance/{id}", ids.unknown()),
        // Condition
        expect(200, Condition.class, "/api/Condition/{id}", ids.condition()),
        expect(404, OperationOutcome.class, "/api/Condition/{id}", ids.unknown()),
        // DiagnosticReport
        expect(200, DiagnosticReport.class, "/api/DiagnosticReport/{id}", ids.diagnosticReport()),
        expect(404, OperationOutcome.class, "/api/DiagnosticReport/{id}", ids.unknown()),
        expect(
            200,
            DiagnosticReport.Bundle.class,
            "api/DiagnosticReport?_id={id}",
            ids.diagnosticReport()),
        expect(404, OperationOutcome.class, "api/DiagnosticReport?_id={id}", ids.unknown()),
        expect(
            200,
            DiagnosticReport.Bundle.class,
            "api/DiagnosticReport?identifier={id}",
            ids.diagnosticReport()),
        expect(404, OperationOutcome.class, "/api/DiagnosticReport?identifier={id}", ids.unknown()),
        expect(
            200,
            DiagnosticReport.Bundle.class,
            "/api/DiagnosticReport?patient={patient}",
            ids.patient()),
        expect(
            200,
            DiagnosticReport.Bundle.class,
            "/api/DiagnosticReport?patient={patient}&category=LAB",
            ids.patient()),
        expect(
            200,
            DiagnosticReport.Bundle.class,
            "/api/DiagnosticReport?patient={patient}&code={loinc1}",
            ids.patient(),
            ids.diagnosticReports().loinc1()),
        expect(
            200,
            DiagnosticReport.Bundle.class,
            "/api/DiagnosticReport?patient={patient}&code={loinc1},{loinc2}",
            ids.patient(),
            ids.diagnosticReports().loinc1(),
            ids.diagnosticReports().loinc2()),
        expect(
            200,
            DiagnosticReport.Bundle.class,
            "/api/DiagnosticReport?patient={patient}&category=LAB&date={onDate}",
            ids.patient(),
            ids.diagnosticReports().onDate()),
        expect(
            200,
            DiagnosticReport.Bundle.class,
            "/api/DiagnosticReport?patient={patient}&category=LAB&date={fromDate}&date={toDate}",
            ids.patient(),
            ids.diagnosticReports().fromDate(),
            ids.diagnosticReports().toDate()),
        // Immunization
        expect(200, Immunization.class, "/api/Immunization/{id}", ids.immunization()),
        expect(404, OperationOutcome.class, "/api/Immunization/{id}", ids.unknown()),
        expect(200, Immunization.Bundle.class, "/api/Immunization?_id={id}", ids.immunization()),
        expect(
            200,
            Immunization.Bundle.class,
            "/api/Immunization?identifier={id}",
            ids.immunization()),
        expect(404, OperationOutcome.class, "/api/Immunization?_id={id}", ids.unknown()),
        expect(
            200, Immunization.Bundle.class, "/api/Immunization?patient={patient}", ids.patient()),
        // Medication
        expect(200, Medication.class, "/api/Medication/{id}", ids.medication()),
        expect(404, OperationOutcome.class, "/api/Medication/{id}", ids.unknown()),
        expect(200, Medication.Bundle.class, "/api/Medication?_id={id}", ids.medication()),
        expect(200, Medication.Bundle.class, "/api/Medication?identifier={id}", ids.medication()),
        expect(404, OperationOutcome.class, "/api/Medication?_id={id}", ids.unknown()),
        // Observation
        expect(200, Observation.class, "/api/Observation/{id}", ids.observation()),
        expect(404, OperationOutcome.class, "/api/Observation/{id}", ids.unknown()),
        expect(200, Observation.Bundle.class, "/api/Observation?_id={id}", ids.observation()),
        expect(
            200, Observation.Bundle.class, "/api/Observation?identifier={id}", ids.observation()),
        expect(404, OperationOutcome.class, "/api/Observation?_id={id}", ids.unknown()),
        expect(200, Observation.Bundle.class, "/api/Observation?patient={patient}", ids.patient()),
        expect(
            200,
            Observation.Bundle.class,
            "/api/Observation?patient={patient}&category=laboratory",
            ids.patient()),
        expect(
            200,
            Observation.Bundle.class,
            "/api/Observation?patient={patient}&category=laboratory&date={date}",
            ids.patient(),
            ids.observations().onDate()),
        expect(
            200,
            Observation.Bundle.class,
            "/api/Observation?patient={patient}&category=laboratory&date={from}&date={to}",
            ids.patient(),
            ids.observations().dateRange().from(),
            ids.observations().dateRange().to()),
        expect(
            200,
            Observation.Bundle.class,
            "/api/Observation?patient={patient}&category=vital-signs",
            ids.patient()),
        expect(
            200,
            Observation.Bundle.class,
            "/api/Observation?patient={patient}&category=laboratory,vital-signs",
            ids.patient()),
        expect(
            200,
            Observation.Bundle.class,
            "/api/Observation?patient={patient}&code={loinc1}",
            ids.patient(),
            ids.observations().loinc1()),
        expect(
            200,
            Observation.Bundle.class,
            "/api/Observation?patient={patient}&code={loinc1},{loinc2}",
            ids.patient(),
            ids.observations().loinc1(),
            ids.observations().loinc2()),
        // Patient
        expect(200, Patient.class, "/api/Patient/{id}", ids.patient()),
        expect(404, OperationOutcome.class, "/api/Patient/{id}", ids.unknown()),
        expect(200, Patient.Bundle.class, "/api/Patient?_id={id}", ids.patient()),
        expect(200, Patient.Bundle.class, "/api/Patient?identifier={id}", ids.patient()),
        expect(404, OperationOutcome.class, "/api/Patient?_id={id}", ids.unknown()),
        expect(
            200,
            Patient.Bundle.class,
            "/api/Patient?family={family}&gender={gender}",
            ids.pii().family(),
            ids.pii().gender()),
        expect(
            200,
            Patient.Bundle.class,
            "/api/Patient?given={given}&gender={gender}",
            ids.pii().given(),
            ids.pii().gender()),
        expect(
            200,
            Patient.Bundle.class,
            "/api/Patient?name={name}&birthdate={birthdate}",
            ids.pii().name(),
            ids.pii().birthdate()),
        expect(
            200,
            Patient.Bundle.class,
            "/api/Patient?name={name}&gender={gender}",
            ids.pii().name(),
            ids.pii().gender())
        //
        );
  }

  private TestClient argonaut() {
    return Sentinel.get().clients().argonaut();
  }

  @Test
  public void getResource() {
    argonaut().get(path, params).expect(status).expectValid(response);
  }
}
