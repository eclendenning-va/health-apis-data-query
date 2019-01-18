package gov.va.health.api.sentinel.crawler;

import gov.va.health.api.sentinel.crawler.Result.Outcome;
import java.util.Collection;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;

/** This collector will provide a text summary upon completion. */
@RequiredArgsConstructor(staticName = "wrap")
public class SummarizingResultCollector implements ResultCollector {

  private final ResultCollector delegate;

  private final Collection<Result.Summary> summaries = new ConcurrentLinkedQueue<>();

  private final AtomicInteger failures = new AtomicInteger(0);

  @Override
  public void add(Result result) {
    summaries.add(result.summarize());
    if (result.outcome() != Outcome.OK) {
      failures.incrementAndGet();
    }
    delegate.add(result);
  }

  @Override
  public void done() {
    delegate.done();
  }

  public int failures() {
    return failures.get();
  }

  @Override
  public void init() {
    delegate.init();
  }

  /** Return a message suitable to being printed to the console. */
  public String message() {
    StringBuilder message = new StringBuilder();
    message.append("Outcomes");
    message.append("\n--------------------");
    for (Outcome outcome : Outcome.values()) {
      message.append("\n").append(outcome).append(": ").append(queriesWithOutcome(outcome).count());
    }
    message.append("\n--------------------");
    message.append("\nTotal: ").append(summaries.size());
    message.append("\nFailures: ").append(failures.get());
    if (failures.get() > 0) {
      message.append("\nFAILURE");
    } else {
      message.append("\nSUCCESS");
    }
    return message.toString();
  }

  private Stream<String> queriesWithOutcome(Result.Outcome outcome) {
    return summaries.stream().filter(s -> s.outcome() == outcome).map(Result.Summary::query);
  }
}
