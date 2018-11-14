package gov.va.health.api.sentinel;

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
  public void patientSearchById() {
    argonaut().get("/api/Patient?_id={id}", ids().patient()).expect(200);
  }

  @Test
  public void patientSearchByNameAndBirthdate() {
    argonaut()
        .get("/api/Patient?name={name}&birthdate={birthdate}", ids().name(), ids().birthdate())
        .expect(200);
  }

  @Test
  public void patientSearchByFamilyAndGender() {
    argonaut()
        .get("/api/Patient??family={family}&gender={gender}", ids().family(), ids().gender())
        .expect(200);
  }

  @Test
  public void patientSearchByNameAndGender() {
    argonaut()
        .get("/api/Patient?name={name}&gender={gender}", ids().name(), ids().gender())
        .expect(200);
  }

  @Test
  public void patientSearchByGivenAndGender() {
    argonaut()
        .get("/api/Patient?given={given}&gender={gender}", ids().given(), ids().gender())
        .expect(200);
  }

  @Test
  public void patientSearchByName() {
    argonaut().get("/api/Patient?_id={id}", ids().patient()).expect(200);
  }

  @Test
  public void patientReadUnknown() {
    argonaut()
        .get("/api/Patient/{id}", ids().unknown())
        .expect(404)
        .expectValid(OperationOutcome.class);
  }

  @Test
  public void patientSearchUnknown() {
    argonaut()
        .get("/api/Patient?_id={id}", ids().unknown())
        .expect(404)
        .expectValid(OperationOutcome.class);
  }
}
