package gov.va.health.api.sentinel;

import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

/** Collection of IDs needed by the tests. */
@Value
@Builder(toBuilder = true)
public class TestIds {
  @NonNull String patient;
  @NonNull String medication;
  @NonNull String diagnosticReport;
  @NonNull String observation;
  @NonNull String unknown;
  @NonNull PersonallyIdentifiableInformation pii;
  @NonNull Observations observations;

  @Value
  @Builder
  public static class Observations {
    @NonNull Range dateRange;
    @NonNull String onDate;
    @NonNull String loinc1;
    @NonNull String loinc2;
  }

  @Value
  @Builder
  public static class PersonallyIdentifiableInformation {
    @NonNull String name;
    @NonNull String given;
    @NonNull String family;
    @NonNull String birthdate;
    @NonNull String gender;
  }

  @Value
  @Builder
  @AllArgsConstructor(staticName = "of")
  public static class Range {
    @NotNull String from;
    @NotNull String to;

    public static final Range allTime() {
      return Range.of("gt1970-01-01", "lt2038-01-19");
    }
  }
}
