package gov.va.health.api.sentinel;

import io.restassured.response.Response;
import io.restassured.http.Method;
import java.util.PriorityQueue;
import java.util.Queue;
import lombok.Builder;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;

@Value
@Builder
@Slf4j
public class ConcurrentRequestQueue implements RequestQueue {

  private final ServiceDefinition service;

  @Override
  public ExpectedResponse get(String path) {
    Response baselineRequest = get("application/json", path);
    return ExpectedResponse.of(baselineRequest);
  }

  Response get(String contentType, String path) {
    return service()
        .requestSpecification()
        .contentType(contentType)
        .request(Method.GET,path);
  }

  /** Features to come back to. */
  /*ValueMap<String, Integer> counts;
  ConcurrentSkipListSet completed;

  public void populateMap() {
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
