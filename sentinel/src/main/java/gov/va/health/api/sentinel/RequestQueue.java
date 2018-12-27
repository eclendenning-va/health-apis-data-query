package gov.va.health.api.sentinel;

import java.util.Queue;

/** The RequestQueue holds the Queue utilized by the Crawler. */
public interface RequestQueue {
  String next();

  boolean hasNext();

  void add(String url);

  Queue<String> getQueue();
}
