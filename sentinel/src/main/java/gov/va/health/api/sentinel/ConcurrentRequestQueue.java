package gov.va.health.api.sentinel;

import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentSkipListSet;

/** Provides a thread safe implementation of the request queue. */
public class ConcurrentRequestQueue implements RequestQueue {

  /** The current items in the queue are stored here. */
  private final Queue<String> queries = new ConcurrentLinkedQueue<>();
  /**
   * All items that have ever been stored in the queue are stored here. This is used to prevent
   * duplicate entries.
   */
  private final Set<String> used = new ConcurrentSkipListSet<>();

  @Override
  public void add(String url) {
    if (!used.contains(url)) {
      queries.add(url);
      used.add(url);
    }
  }

  @Override
  public boolean hasNext() {
    if (queries.isEmpty() || queries.peek() == null) {
      return false;
    }
    return true;
  }

  @Override
  public String next() {
    if (queries.isEmpty()) {
      throw new IllegalStateException();
    }
    return queries.poll();
  }
}
