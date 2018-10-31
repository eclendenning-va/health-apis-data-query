package gov.va.api.health.argonaut.service.patient;

import gov.va.api.health.argonaut.api.Patient;
import gov.va.dvp.cdw.xsd.pojos.Patient103Root;
import org.springframework.stereotype.Service;

@Service
public class PatientTransformer implements PatientController.PatientTransformer {

  @Override
  public Patient apply(Patient103Root.Patients.Patient patient) {
    return Patient.builder().id(patient.getCdwId()).build();
  }
}
