package gov.va.health.api.sentinel.crawler;

/** The result collector will manage the individual results from the crawler and give a summary. */
public interface ResultCollector {
  /** Initalize the result collector and do any prep needed for collecting results. */
  void init();

  /** Add a result of a query that has been crawled. */
  void add(Result result);

  /** Done with adding results, do any necessary clean up and write summary of results. */
  void done();
}
