package gov.va.health.api.sentinel.crawler;

import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
import lombok.extern.slf4j.Slf4j;

/**
 * A implementation of the results collector that prints each result to a file and creates a
 * separate summary file to give information about all of the results.
 */
@Slf4j
public class FileResultsCollector implements ResultCollector {
  static final Set<String> results = new ConcurrentSkipListSet<>();

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
    results.add(basicInfo);
    log.info("Result: {}", basicInfo);
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
      params = searchParts[1];
    }
    resourceName = resourceName.replaceAll("([a-z]{2})([a-z]+)", "$1");
    String filename = resourceName + params;
    return filename.replaceAll("[^A-Za-z0-9]", "");
  }

  @Override
  public void done() {
    results.clear();
  }

  @Override
  public void init() {}
}
