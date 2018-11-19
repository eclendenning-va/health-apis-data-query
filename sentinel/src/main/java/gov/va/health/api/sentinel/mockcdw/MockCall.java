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

  private String aliasOf(String key) {
    if ("_id".equals(key)) {
      return "identifier";
    }
    return key;
  }

  boolean matches(MockEntries.Entry e) {

    if (!e.query().startsWith(resourceAndVersion())) {
      return false;
    }

    MultiValueMap<String, String> entryParams = parametersOf(e.query());
    if (!parametersOf(fhirString()).equals(entryParams)) {
      return false;
    }
    if (entryParams.size() == 1 && entryParams.keySet().contains("identifier")) {
      /*
       * Page and count don't matter for read
       */
      return true;
    }

    return e.page() == page() && e.count() == count();
  }

  private MultiValueMap<String, String> parametersOf(String resourceVersionParams) {
    String[] parts = resourceVersionParams.split("\\?");
    MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
    if (parts.length > 1) {
      Arrays.stream(parts[1].split("&"))
          .filter(p -> !p.startsWith("page=") && !p.startsWith("_count="))
          .map(kv -> kv.split("="))
          .forEach(kv -> params.add(aliasOf(kv[0]), kv[1]));
    }
    return params;
  }

  private String resourceAndVersion() {
    return fhirString.split("\\?")[0];
  }
}
