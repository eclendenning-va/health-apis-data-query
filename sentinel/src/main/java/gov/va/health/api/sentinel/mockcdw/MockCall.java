package gov.va.health.api.sentinel.mockcdw;

import java.util.Arrays;
import lombok.Data;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

/**
 * Stored procedure call parameters. These will be matched in the index and the corresponding file
 * will be matched.
 */
@Data
class MockCall {
  private int page;
  private int count;
  private String fhirString;

  boolean matches(MockEntries.Entry e) {
    return e.page() == page()
        && e.count() == count()
        && e.query().startsWith(resourceAndVersion())
        && parametersOf(fhirString()).equals(parametersOf(e.query()));
  }

  private MultiValueMap<String, String> parametersOf(String resourceVersionParams) {
    String[] parts = resourceVersionParams.split("\\?");
    MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
    if (parts.length > 1) {
      Arrays.stream(parts[1].split("&"))
          .filter(p -> !p.startsWith("page=") && !p.startsWith("_count="))
          .map(kv -> kv.split("="))
          .forEach(kv -> params.add(kv[0], kv[1]));
    }
    return params;
  }

  String resourceAndVersion() {
    return fhirString.split("\\?")[0];
  }
}
