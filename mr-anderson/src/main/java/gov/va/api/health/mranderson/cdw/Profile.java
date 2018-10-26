package gov.va.api.health.mranderson.cdw;

import java.util.Locale;
import lombok.NonNull;

/** The FHIR profile used to determine the data returned by the searches. */
public enum Profile {
  ARGONAUT,
  DSTU2,
  STU3;

  public static Profile fromValue(@NonNull String value) {
    return valueOf(value.toUpperCase(Locale.ENGLISH));
  }
}
