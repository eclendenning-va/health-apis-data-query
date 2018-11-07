package gov.va.health.api.sentinel;

import gov.va.api.health.argonaut.api.Patient;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

@Slf4j
public class ArgonautIT {

  private IdRegistrar registrar = IdRegistrar.of(Sentinel.get().system());

  private TestClient argonaut() {
    return Sentinel.get().clients().argonaut();
  }

  @Test
  public void deleteMePatientRead() {
    log.error("oh");
    argonaut().get("/api/Patient/{id}", ids().patient()).expect(200).expectValid(Patient.class);
  }

  private TestIds ids() {
    return registrar.registeredIds();
  }
}
