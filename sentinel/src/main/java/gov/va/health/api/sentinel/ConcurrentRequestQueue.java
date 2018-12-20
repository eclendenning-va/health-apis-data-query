package gov.va.health.api.sentinel;

import java.util.PriorityQueue;
import java.util.Queue;

public class ConcurrentRequestQueue implements RequestQueue {

  Queue<String> queries = setOriginalQueries();

  @Override
  public String next() {
    return queries.poll();
  }

  @Override
  public boolean hasNext() {
    if (queries.poll() == null) {
      return false;
    }
    return true;
  }

  @Override
  public void add(String url) {
    queries.add(url);
  }

  /** Hardcoded temporarily. */
  Queue<String> setOriginalQueries() {
    Queue<String> queue = new PriorityQueue<>();
    queue.add("https://localhost:8090/api/AllergyIntolerance?patient=185601V825290");
    queue.add("https://localhost:8090/api/Condition?patient=185601V825290");
    queue.add("https://localhost:8090/api/DiagnosticReport?patient=185601V825290");
    queue.add("https://localhost:8090/api/Immunization?patient=185601V825290");
    queue.add("https://localhost:8090/api/Medication?patient=185601V825290");
    queue.add("https://localhost:8090/api/MedicationOrder?patient=185601V825290");
    queue.add("https://localhost:8090/api/MedicationStatement?patient=185601V825290");
    queue.add("https://localhost:8090/api/Observation?patient=185601V825290");
    queue.add("https://localhost:8090/api/Procedure?patient=185601V825290");
    return queue;
  }

  /** Features to come back to. */
  /*ValueMap<String, Integer> counts;
  ConcurrentSkipListSet completed;

  public void startCount() {
    counts.add("AllergyIntolerance", 0);
    counts.add("Appointment", 0);
    counts.add("Condition", 0);
    counts.add("DiagnosticReport", 0);
    counts.add("Encounter", 0);
    counts.add("Immunization", 0);
    counts.add("Location", 0);
    counts.add("Medication", 0);
    counts.add("MedicationOrder", 0);
    counts.add("MedicationStatement", 0);
    counts.add("Observation", 0);
    counts.add("Organization", 0);
    counts.add("Patient", 0);
    counts.add("Practitioner", 0);
    counts.add("Procedure", 0);
  }*/
}
