package gov.va.api.health.argonaut.service.mranderson;

import gov.va.api.health.argonaut.api.Patient;

public interface MrAndersonClient {

  Patient query(MrAndersonQuery queryString);
}
