package gov.va.api.health.argonaut.service.mranderson;

import gov.va.api.health.argonaut.api.Patient;
import org.springframework.util.MultiValueMap;

import java.util.List;

public interface MrAndersonClient {

  List<Patient> query(MultiValueMap<String, String> parameterMap);

  enum Profile {
    ARGONAUT,
    DSTU2,
    STU3
  }
}
