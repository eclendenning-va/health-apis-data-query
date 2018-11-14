package gov.va.health.api.sentinel;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

/** Collection of IDs needed by the tests. */
@Builder
@Value
public class TestIds {
  @NonNull String patient;
  @NonNull String name;
  @NonNull String birthdate;
  @NonNull String gender;
  @NonNull String given;
  @NonNull String family;
  @NonNull String unknown;
}
