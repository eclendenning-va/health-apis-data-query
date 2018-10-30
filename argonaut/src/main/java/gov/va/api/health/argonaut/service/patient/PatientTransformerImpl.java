package gov.va.api.health.argonaut.service.patient;

import gov.va.api.health.argonaut.api.Patient;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PatientTransformerImpl implements PatientTransformer {

  @Override
  public Patient apply(List<Patient> patientBundle) {
    return Patient.builder().id("One-two-three is " + patientBundle.get(0).id()).build();
  }
}
