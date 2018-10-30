package gov.va.api.health.argonaut.service.patient;

import gov.va.api.health.argonaut.api.Patient;
import java.util.function.Function;

public interface PatientTransformer extends Function<Patient, Patient> {}
