package gov.va.api.health.dataquery.service.controller.patient;

import gov.va.api.health.argonaut.api.resources.Patient;
import gov.va.api.health.argonaut.api.resources.Patient.Bundle;
import gov.va.api.health.argonaut.api.resources.Patient.Entry;
import gov.va.api.health.dataquery.service.controller.AbstractIncludesIcnMajig;
import java.util.stream.Stream;
import org.springframework.web.bind.annotation.ControllerAdvice;

/**
 * Intercept all RequestMapping payloads of Type Patient.class or Bundle.class. Extract ICN(s) from
 * these payloads with the provided function. This will lead to populating the X-VA-INCLUDES-ICN
 * header.
 */
@ControllerAdvice
public class PatientIncludesIcnMajig extends AbstractIncludesIcnMajig<Patient, Entry, Bundle> {

  public PatientIncludesIcnMajig() {
    super(Patient.class, Bundle.class, (body) -> Stream.of(body.id()));
  }
}
