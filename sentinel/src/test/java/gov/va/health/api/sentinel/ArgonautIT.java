package gov.va.health.api.sentinel;

import gov.va.api.health.argonaut.api.Medication;
import gov.va.api.health.argonaut.api.OperationOutcome;
import gov.va.api.health.argonaut.api.Patient;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

@Slf4j
public class ArgonautIT {

  private IdRegistrar registrar = IdRegistrar.of(Sentinel.get().system());

  private TestClient argonaut() {
    return Sentinel.get().clients().argonaut();
  }

  private TestIds ids() {
    return registrar.registeredIds();
  }

  @Test
  public void patientRead() {
    argonaut().get("/api/Patient/{id}", ids().patient()).expect(200).expectValid(Patient.class);
  }

  @Test
  public void medicationRead() {
    argonaut()
        .get("/api/Medication/{id}", ids().medication())
        .expect(200)
        .expectValid(Medication.class);
  }

  @Test
  public void patientReadUnknown() {
    argonaut()
        .get("/api/Patient/{id}", ids().unknown())
        .expect(404)
        .expectValid(OperationOutcome.class);
  }

  @Test
  public void medicationReadUnknown() {
    argonaut()
        .get("/api/Medication/{id}", ids().unknown())
        .expect(404)
        .expectValid(OperationOutcome.class);
  }

  @Test
  public void patientSearchByFamilyAndGender() {
    argonaut()
        .get(
            "/api/Patient?family={family}&gender={gender}",
            ids().pii().family(),
            ids().pii().gender())
        .expect(200);
  }

  @Test
  public void patientSearchByGivenAndGender() {
    argonaut()
        .get(
            "/api/Patient?given={given}&gender={gender}", ids().pii().given(), ids().pii().gender())
        .expect(200);
  }

  @Test
  public void patientSearchById() {
    argonaut().get("/api/Patient?_id={id}", ids().patient()).expect(200);
  }

  @Test
  public void patientSearchByName() {
    argonaut().get("/api/Patient?_id={id}", ids().patient()).expect(200);
  }

  @Test
  public void patientSearchByNameAndBirthdate() {
    argonaut()
        .get(
            "/api/Patient?name={name}&birthdate={birthdate}",
            ids().pii().name(),
            ids().pii().birthdate())
        .expect(200);
  }

  @Test
  public void patientSearchByNameAndGender() {
    argonaut()
        .get("/api/Patient?name={name}&gender={gender}", ids().pii().name(), ids().pii().gender())
        .expect(200);
  }

  @Test
  public void patientSearchUnknown() {
    argonaut()
        .get("/api/Patient?_id={id}", ids().unknown())
        .expect(404)
        .expectValid(OperationOutcome.class);
  }
}
