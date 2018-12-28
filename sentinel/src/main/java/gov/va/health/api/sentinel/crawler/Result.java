package gov.va.health.api.sentinel.crawler;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Builder
@Data
public class Result {
  @NonNull String query;
  HttpStatus httpStatus;
  String body;
  @NonNull Health health;
  String additionalInfo;

  @SuppressWarnings("unused")
  public enum Health {
    INVALID_STATUS,
    INVALID_PAYLOAD,
    OK,
    REQUEST_FAILED,
    INVALID_URL
  }
}
