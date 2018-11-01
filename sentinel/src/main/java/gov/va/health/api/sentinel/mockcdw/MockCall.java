package gov.va.health.api.sentinel.mockcdw;

import java.util.Arrays;
import java.util.stream.Collectors;
import lombok.Data;

/**
 * Stored procedure call parameters. These will be matched in the index and the corresponding file
 * will be matched.
 */
@Data
class MockCall {
  private int page;
  private int count;
  private String fhirString;

  /**
   * Provided a sorted query where parameters are sorted and `page` and `_count` will be removed.
   */
  String fhirStringWithoutPaging() {
    String[] parts = fhirString.split("\\?");
    String params = "";
    if (parts.length > 1) {
      params =
          Arrays.stream(parts[1].split("&"))
              .filter(p -> !p.startsWith("page=") && !p.startsWith("_count="))
              .sorted()
              .collect(Collectors.joining("&"));
    }
    return parts[0] + "?" + params;
  }
}
