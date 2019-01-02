package gov.va.health.api.sentinel.crawler;

import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

/** The result of the query. */
@AllArgsConstructor
@Builder
@Value
public class Result {

  @NonNull Instant timestamp;
  @NonNull String query;
  /**
   * HTTP status will be 0 if we failed to communicate with the server, otherwise it will be result
   * of the call.
   */
  int httpStatus;

  String body;
  @NonNull Outcome outcome;
  String additionalInfo;

  @SuppressWarnings("unused")
  public enum Outcome {
    INVALID_PAYLOAD,
    INVALID_STATUS,
    INVALID_URL,
    OK,
    REQUEST_FAILED
  }
}
