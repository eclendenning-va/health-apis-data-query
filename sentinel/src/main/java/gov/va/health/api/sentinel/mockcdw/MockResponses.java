package gov.va.health.api.sentinel.mockcdw;

import gov.va.health.api.sentinel.mockcdw.MockEntries.Entry;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Data;
import lombok.Value;

/** The look up engine matches stored procedure calls to indexed samples. */
@Value
@Builder
class MockResponses {
  File baseDirectory;
  MockEntries entries;

  private Predicate<Entry> forCall(MockCall call) {
    return e ->
        e.page() == call.page()
            && e.count() == call.count()
            && e.query().equals(call.fhirStringWithoutPaging());
  }

  /** Return the XML sample contents for the call. */
  String responseFor(MockCall call) {
    return entries
        .entries()
        .stream()
        .filter(forCall(call))
        .findFirst()
        .map(toFileContents())
        .orElseThrow(() -> new FailedMockResponse("No sample found: " + call.toString()));
  }

  private Function<Entry, String> toFileContents() {
    return entry -> {
      try {
        return new String(
            Files.readAllBytes(new File(baseDirectory, entry.file()).toPath()),
            StandardCharsets.UTF_8);
      } catch (IOException e) {
        throw new FailedMockResponse("Failed to read file contents: " + entry, e);
      }
    };
  }

  static class FailedMockResponse extends RuntimeException {

    public FailedMockResponse(String message) {
      super(message);
    }

    public FailedMockResponse(String message, Throwable cause) {
      super(message, cause);
    }
  }

  /**
   * Stored procedure call parameters. These will be matched in the index and the corresponding file
   * will be matched.
   */
  @Data
  static class MockCall {
    private int page;
    private int count;
    private String fhirString;

    /**
     * Provided a sorted query where parameters are sorted and `page` and `_count` will be removed.
     */
    private String fhirStringWithoutPaging() {
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
}
