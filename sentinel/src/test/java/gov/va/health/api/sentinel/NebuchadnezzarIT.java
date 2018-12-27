package gov.va.health.api.sentinel;

import java.util.PriorityQueue;
import java.util.Queue;

public class NebuchadnezzarIT {

  //  RequestQueue requestQueue = new ConcurrentRequestQueue();
  //  TestQueueData testQueueData = TestQueueData.get();
  //  Queue<String> testQueue = testQueueData.createTestQueue();
  //
  //  @Test
  //  public void fullQueueTest() {
  //    assertThat(requestQueue.getQueue()).isEqualTo(testQueue);
  //    assertThat(requestQueue.next()).isEqualTo(testQueue.poll());
  //    assertThat(requestQueue.hasNext()).isEqualTo(true);
  //    requestQueue.add("https://localhost:8090/api/AllergyIntolerance?patient=185601V825290");
  //    testQueue.add("https://localhost:8090/api/AllergyIntolerance?patient=185601V825290");
  //    // assertThat(requestQueue.getQueue()).isEqualTo(testQueue);
  //  }
  //
  //  @Test
  //  public void nullQueueTest() {
  //    requestQueue.getQueue().clear();
  //    assertThat(requestQueue.hasNext()).isEqualTo(false);
  //    assertThat(requestQueue.next()).isNull();
  //    assertThat(requestQueue.getQueue()).isNull();
  //  }
  //
  //  @Test
  //  @Ignore
  //  public void testCrawl() {
  //    Crawler crawler = new Crawler();
  //    crawler.crawl();
  //  }
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
  //
  //  @NoArgsConstructor(staticName = "get")
  //  public static class TestQueueData {
  //
  //    public Queue<String> createTestQueue() {
  //      Queue<String> testQueue = new PriorityQueue<>();
  //      testQueue.add("https://localhost:8090/api/AllergyIntolerance?patient=185601V825290");
  //      testQueue.add("https://localhost:8090/api/Condition?patient=185601V825290");
  //      testQueue.add("https://localhost:8090/api/DiagnosticReport?patient=185601V825290");
  //      testQueue.add("https://localhost:8090/api/Immunization?patient=185601V825290");
  //      testQueue.add("https://localhost:8090/api/Medication?patient=185601V825290");
  //      testQueue.add("https://localhost:8090/api/MedicationOrder?patient=185601V825290");
  //      testQueue.add("https://localhost:8090/api/MedicationStatement?patient=185601V825290");
  //      testQueue.add("https://localhost:8090/api/Observation?patient=185601V825290");
  //      testQueue.add("https://localhost:8090/api/Procedure?patient=185601V825290");
  //      return testQueue;
  //    }
  //  }
}
