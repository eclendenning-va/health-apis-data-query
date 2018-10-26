package gov.va.api.health.argonaut.service.patient;

import gov.va.api.health.argonaut.api.Patient;
import org.springframework.stereotype.Service;

@Service
public class PatientTransformerImpl implements PatientTransformer {

    @Override
    public Patient apply(Patient patient) {
        return Patient.builder().id("One-two-three is " + patient.id()).build();
    }
}
