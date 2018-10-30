package gov.va.api.health.argonaut.service.patient;

import gov.va.api.health.argonaut.api.Patient;

import java.util.List;
import java.util.function.Function;

public interface PatientTransformer extends Function<List<Patient>, Patient> {}
