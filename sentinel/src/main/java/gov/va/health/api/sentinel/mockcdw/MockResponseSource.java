package gov.va.health.api.sentinel.mockcdw;

import gov.va.health.api.sentinel.mockcdw.MockEntries.Entry;
import gov.va.health.api.sentinel.mockcdw.MockResponses.FailedMockResponse;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import lombok.Builder;
import lombok.Value;

/** The look up engine matches stored procedure calls to indexed samples. */
@Value
@Builder
class MockResponseSource {
  File baseDirectory;
  MockEntries entries;

  private Predicate<Entry> forCall(MockCall call) {
    return e ->
        e.page() == call.page()
            && e.count() == call.count()
            && e.query().equals(call.fhirStringWithoutPaging());
  }

  /** Return the XML sample contents for the call. */
  Optional<String> responseFor(MockCall call) {
    return entries.entries().stream().filter(forCall(call)).findFirst().map(toFileContents());
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
}
