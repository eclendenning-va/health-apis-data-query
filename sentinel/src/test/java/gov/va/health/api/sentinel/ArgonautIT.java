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
}
