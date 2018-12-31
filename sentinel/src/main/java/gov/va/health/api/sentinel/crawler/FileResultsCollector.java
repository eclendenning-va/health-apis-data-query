package gov.va.health.api.sentinel.crawler;

import gov.va.health.api.sentinel.crawler.Result.Outcome;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;

/**
 * A implementation of the results collector that prints each result to a file and creates a
 * separate summary file to give information about all of the results.
 */
@Slf4j
public class FileResultsCollector implements ResultCollector {
  static final Set<String> summary = new ConcurrentSkipListSet<>();

  @Override
  public void add(Result result) {
    String basicInfo =
        result.timestamp().toEpochMilli()
            + ","
            + createFilename(result.query())
            + ","
            + result.outcome()
            + ","
            + result.query();
    summary.add(basicInfo);
    log.info("{} {}", result.query(), result.outcome());
    if (result.outcome() != Outcome.OK) {
      log.error("{}", result.body());
      log.error("{}", result.additionalInfo());
    }
  }

  /**
   * Creates small filename from a query, uses slashes to split for reads and question marks for
   * searches.
   */
  private String createFilename(String query) {
    String[] splitQuery = query.split("/");
    String resourceName = splitQuery[splitQuery.length - 2];
    String params = splitQuery[splitQuery.length - 1];
    if (params.contains("?")) {
      String[] searchParts = params.split("\\?");
      resourceName = searchParts[0];
      params = searchParts[1].replaceAll("patient", "P");
    }
    resourceName = resourceName.replaceAll("([a-z]{2})([a-z]+)", "$1");
    String filename = resourceName + params;
    return filename.replaceAll("[^A-Za-z0-9]", "");
  }

  @Override
  public void done() {
    log.info("Summary\n{}:", summary.stream().collect(Collectors.joining("\n")));
    log.info("Made {} requests", summary.size());
  }

  @Override
  public void init() {}
}
