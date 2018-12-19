package gov.va.health.api.sentinel;

import java.util.PriorityQueue;
import java.util.Queue;

public class Crawler  {

  /** Crawling. */
  public void crawl() {
    Queue<String> queries = setOriginalQueries();
    String query = queries.poll();
    System.out.println(query);

  }

  /** Hardcoded temporarily. */
  Queue<String> setOriginalQueries() {
    Queue<String> queries = new PriorityQueue<>();
    queries.add("https://localhost:8090/api/AllergyIntolerance?patient=185601V825290");
    queries.add("https://localhost:8090/api/Condition?patient=185601V825290");
    queries.add("https://localhost:8090/api/DiagnosticReport?patient=185601V825290");
    queries.add("https://localhost:8090/api/Immunization?patient=185601V825290");
    queries.add("https://localhost:8090/api/Medication?patient=185601V825290");
    queries.add("https://localhost:8090/api/MedicationOrder?patient=185601V825290");
    queries.add("https://localhost:8090/api/MedicationStatement?patient=185601V825290");
    queries.add("https://localhost:8090/api/Observation?patient=185601V825290");
    queries.add("https://localhost:8090/api/Procedure?patient=185601V825290");
    return queries;
  }
}
