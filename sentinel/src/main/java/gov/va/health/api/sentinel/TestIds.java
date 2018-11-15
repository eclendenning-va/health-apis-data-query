package gov.va.health.api.sentinel;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

/** Collection of IDs needed by the tests. */
@Value
@Builder
public class TestIds {
  @NonNull String patient;
  @NonNull String medication;
  @NonNull String unknown;
  @NonNull PersonallyIdentifiableInformation pii;

  @Value
  @Builder
  public static class PersonallyIdentifiableInformation {
    @NonNull String name;
    @NonNull String given;
    @NonNull String family;
    @NonNull String birthdate;
    @NonNull String gender;
  }
}
